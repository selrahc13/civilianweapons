/**
    Copyright (C) <2015> <coolAlias>

    This file is part of coolAlias' Dynamic Sword Skills Minecraft Mod; as such,
    you can redistribute it and/or modify it under the terms of the GNU
    General Public License as published by the Free Software Foundation,
    either version 3 of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package dynamicswordskills.skills;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.DirtyEntityAccessor;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dynamicswordskills.client.DSSClientEvents;
import dynamicswordskills.client.DSSKeyHandler;
import dynamicswordskills.entity.DSSPlayerInfo;
import dynamicswordskills.network.PacketDispatcher;
import dynamicswordskills.network.bidirectional.ActivateSkillPacket;
import dynamicswordskills.network.bidirectional.AttackTimePacket;
import dynamicswordskills.ref.Config;
import dynamicswordskills.ref.ModInfo;
import dynamicswordskills.util.PlayerUtils;

/**
 * 
 * ENDING BLOW
 * Description: Finish off an enemy made vulnerable by your flurry of blows
 * Activation: Forward, forward, and attack during combo
 * Effect:	Build up combo momentum and then finish off your enemy with a decisive strike,
 * 			gaining bonus xp if successful or becoming flat-footed if not
 * Damage: +(level * 20) percent
 * Duration of vulnerability: 45 - (level * 5) ticks
 * Exhaustion: 2.0F - (level * 0.1F)
 * XP Bonus: level + (value between 1 and the opponent's last remaining health)
 * Special:
 * - May only be used after two or more consecutive strikes on the same target
 * - Slaying an opponent with this move grants additional experience
 * - Failure to slay the target results in not being able to attack for the duration
 * 
 */
public class EndingBlow extends SkillActive
{
	/** Flag for isActive() so that skill can trigger upon impact from LivingHurtEvent */
	private int activeTimer = 0;

	/** Only for vanilla activation: Current number of ticks remaining before skill will not activate */
	@SideOnly(Side.CLIENT)
	private int ticksTilFail;

	/** Number of times the forward key has been pressed this activation cycle */
	@SideOnly(Side.CLIENT)
	private int keyPressed;

	/** Number of consecutive hits the combo had when the skill was last used */
	private int lastNumHits;

	/** Workaround for armor / potions changing damage: checks next tick if entity is dead or not */
	private EntityLivingBase entityHit;

	/** Xp amount to grant if entityHit is dead on update tick */
	private int xp;

	public EndingBlow(String name) {
		super(name);
	}

	private EndingBlow(EndingBlow skill) {
		super(skill);
	}

	@Override
	public EndingBlow newInstance() {
		return new EndingBlow(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> desc, EntityPlayer player) {
		desc.add(getDamageDisplay(level * 20, true) + "%");
		desc.add(getDurationDisplay(getDuration(), true));
		desc.add(getExhaustionDisplay(getExhaustion()));
	}

	@Override
	public boolean isActive() {
		return activeTimer > 0;
	}

	@Override
	protected float getExhaustion() {
		return 2.0F - (level * 0.1F);
	}

	/** Returns the duration of the defense down effect */
	public int getDuration() {
		return 45 - (level * 5);
	}

	@Override
	public boolean canUse(EntityPlayer player) {
		if (!isActive() && super.canUse(player) && PlayerUtils.isHoldingSkillItem(player)) {
			ICombo skill = DSSPlayerInfo.get(player).getComboSkill();
			if (skill != null && skill.isComboInProgress()) {
				if (lastNumHits > 0) {
					return skill.getCombo().getConsecutiveHits() > 1 && skill.getCombo().getSize() > lastNumHits + 2;
				} else {
					return skill.getCombo().getConsecutiveHits() > 1;
				}
			}
		}
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean canExecute(EntityPlayer player) {
		return ticksTilFail > 0 && keyPressed > 1 && canUse(player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isKeyListener(Minecraft mc, KeyBinding key) {
		return (key == mc.gameSettings.keyBindForward || key == DSSKeyHandler.keys[DSSKeyHandler.KEY_ATTACK]
				|| (Config.allowVanillaControls() && key == mc.gameSettings.keyBindAttack));
	}

	/**
	 * Increments the number of times the key has been pressed and starts the fail timer if not yet set,
	 * or triggers the skill if the right conditions are met
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean keyPressed(Minecraft mc, KeyBinding key, EntityPlayer player) {
		if (key == mc.gameSettings.keyBindForward) {
			if (ticksTilFail == 0) {
				ticksTilFail = 6;
			}
			++keyPressed;
		} else if (canExecute(player)) {
			ticksTilFail = 0;
			keyPressed = 0;
			PacketDispatcher.sendToServer(new ActivateSkillPacket(this));
			return true;
		}
		return false;
	}

	@Override
	protected boolean onActivated(World world, EntityPlayer player) {
		activeTimer = 3; // gives server some time for client attack to occur
		ICombo skill = DSSPlayerInfo.get(player).getComboSkill();
		if (skill.getCombo() != null) {
			lastNumHits = skill.getCombo().getSize();
		}
		if (world.isRemote) { // only attack after server has been activated, i.e. client receives activation packet back
			DSSClientEvents.performComboAttack(Minecraft.getMinecraft(), DSSPlayerInfo.get(player).getTargetingSkill());
			ticksTilFail = 0;
			keyPressed = 0;
		}
		return isActive();
	}

	@Override
	protected void onDeactivated(World world, EntityPlayer player) {
		activeTimer = 0;
		entityHit = null;
		xp = 0;
		if (world.isRemote) {
			keyPressed = 0;
			ticksTilFail = 0;
		}
	}

	@Override
	public void onUpdate(EntityPlayer player) {
		if (player.worldObj.isRemote && ticksTilFail > 0) {
			--ticksTilFail;
			if (ticksTilFail == 0) {
				keyPressed = 0;
			}
		}
		if (lastNumHits > 0) {
			if (entityHit != null && xp > 0) {
				updateEntityState(player);
			}
			ICombo skill = DSSPlayerInfo.get(player).getComboSkill();
			if (skill == null || !skill.isComboInProgress()) {
				lastNumHits = 0;
			}
		}
		if (isActive()) {
			--activeTimer;
			if (activeTimer == 0 && !player.worldObj.isRemote && !player.capabilities.isCreativeMode) {
				player.attackTime = getDuration() * 2;
				PacketDispatcher.sendTo(new AttackTimePacket(player.attackTime), (EntityPlayerMP) player);
			}
		}
	}

	/**
	 * Checks if entity hit is dead, granting Xp or causing defensive penalty
	 */
	private void updateEntityState(EntityPlayer player) {
		if (!player.worldObj.isRemote) {
			if (entityHit.getHealth() <= 0.0F) {
				if (entityHit instanceof EntityLiving) {
					DirtyEntityAccessor.setLivingXp((EntityLiving) entityHit, xp, true);
				} else {
					PlayerUtils.spawnXPOrbsWithRandom(player.worldObj, player.worldObj.rand, MathHelper.floor_double(entityHit.posX),
							MathHelper.floor_double(entityHit.posY), MathHelper.floor_double(entityHit.posZ), xp);
				}
			} else {
				PlayerUtils.playSoundAtEntity(player.worldObj, player, ModInfo.SOUND_HURT_FLESH, 0.3F, 0.8F);
				if (!player.worldObj.isRemote && !player.capabilities.isCreativeMode) {
					player.attackTime = getDuration();
					PacketDispatcher.sendTo(new AttackTimePacket(player.attackTime), (EntityPlayerMP) player);
				}
			}
		}
		entityHit = null;
		xp = 0;
	}

	@Override
	public float postImpact(EntityPlayer player, EntityLivingBase entity, float amount) {
		activeTimer = 0;
		ICombo combo = DSSPlayerInfo.get(player).getComboSkill();
		ILockOnTarget lock = DSSPlayerInfo.get(player).getTargetingSkill();
		if (combo != null && combo.isComboInProgress() && lock != null && lock.getCurrentTarget() == combo.getCombo().getLastEntityHit()) {
			amount *= 1.0F + (level * 0.2F);
			PlayerUtils.playSoundAtEntity(player.worldObj, player, ModInfo.SOUND_MORTALDRAW, 0.4F, 0.5F);
			entityHit = entity;
			xp = level + 1 + player.worldObj.rand.nextInt(Math.max(2, MathHelper.ceiling_float_int(entity.getHealth())));
		}
		return amount;
	}
}
