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
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dynamicswordskills.client.DSSKeyHandler;
import dynamicswordskills.entity.DSSPlayerInfo;
import dynamicswordskills.network.PacketDispatcher;
import dynamicswordskills.network.bidirectional.ActivateSkillPacket;
import dynamicswordskills.ref.Config;
import dynamicswordskills.ref.ModInfo;
import dynamicswordskills.util.PlayerUtils;
import dynamicswordskills.util.TargetUtils;

/**
 * 
 * Tap 'down' arrow to parry an incoming attack with chance to disarm opponent. Only works on
 * opponents wielding an item, not against raw physical attacks such as a zombie touch.
 * 
 * Once activated, their is a short window (4 ticks at level 1) during which all incoming
 * attacks will be parried, followed by another short period during which parry cannot be
 * activated again (to prevent spamming). The 'cooldown' time decreases with level, whereas
 * the 'window' time increases.
 * 
 * Chance to Disarm: 0.1F per level + a time bonus of up to 0.2F
 * Exhaustion: 0.3F minus 0.02F per level (0.2F at level 5)
 * Notes: For players of equal parry skill, chance to disarm is based solely on timing
 * 
 * Using vanilla controls, Parry is activated just like the Dodge skill, requiring either a
 * single tap and release, or a double-tap based on the Config settings. Parry never requires
 * a double tap when using the arrow key.
 * 
 */
public class Parry extends SkillActive
{
	/** Timer during which player is considered actively parrying */
	private int parryTimer;

	/** Number of attacks parried this activation cycle */
	private int attacksParried;

	/** Only for double-tap activation: Current number of ticks remaining before skill will not activate */
	@SideOnly(Side.CLIENT)
	private int ticksTilFail;

	/** Notification to play miss sound; set to true when activated and false when attack parried */
	private boolean playMissSound;

	public Parry(String name) {
		super(name);
	}

	private Parry(Parry skill) {
		super(skill);
	}

	@Override
	public Parry newInstance() {
		return new Parry(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(List<String> desc, EntityPlayer player) {
		desc.add(StatCollector.translateToLocalFormatted(getInfoString("info", 1),
				(int)(getDisarmChance(player, null) * 100)));
		desc.add(StatCollector.translateToLocalFormatted(getInfoString("info", 2),
				(int)(2.5F * (getActiveTime() - getParryDelay()))));
		desc.add(StatCollector.translateToLocalFormatted(getInfoString("info", 3), getMaxParries()));
		desc.add(getTimeLimitDisplay(getActiveTime() - getParryDelay()));
		desc.add(getExhaustionDisplay(getExhaustion()));
	}

	@Override
	public boolean isActive() {
		return (parryTimer > 0);
	}

	@Override
	protected float getExhaustion() {
		return 0.3F - (0.02F * level);
	}

	/** Number of ticks that skill will be considered active */
	private int getActiveTime() {
		return 6 + level;
	}

	/** Number of ticks before player may attempt to use this skill again */
	private int getParryDelay() {
		return (5 - (level / 2)); // 2 tick usage window at level 1
	}

	/** The maximum number of attacks that may be parried per use of the skill */
	private int getMaxParries() {
		return (1 + level) / 2;
	}

	/**
	 * Returns player's chance to disarm an attacker
	 * @param attacker if the attacker is an EntityPlayer, their Parry score will decrease their chance
	 * of being disarmed
	 */
	private float getDisarmChance(EntityPlayer player, EntityLivingBase attacker) {
		float penalty = 0.0F;
		float bonus = Config.getDisarmTimingBonus() * (parryTimer > 0 ? (parryTimer - getParryDelay()) : 0);
		if (attacker instanceof EntityPlayer) {
			penalty = Config.getDisarmPenalty() * DSSPlayerInfo.get((EntityPlayer) attacker).getSkillLevel(this);
		}
		return ((level * 0.1F) - penalty + bonus);
	}

	@Override
	public boolean canUse(EntityPlayer player) {
		return super.canUse(player) && !isActive() && PlayerUtils.isHoldingSkillItem(player);
	}

	/**
	 * Only allow activation if player not using item, to prevent clashing with SwordBreak
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public boolean canExecute(EntityPlayer player) {
		return canUse(player) && !PlayerUtils.isUsingItem(player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isKeyListener(Minecraft mc, KeyBinding key) {
		return (key == DSSKeyHandler.keys[DSSKeyHandler.KEY_DOWN] || (Config.allowVanillaControls() && key == mc.gameSettings.keyBindBack));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean keyPressed(Minecraft mc, KeyBinding key, EntityPlayer player) {
		if (canExecute(player)) {
			if (Config.requiresDoubleTap()) {
				if (ticksTilFail > 0) {
					PacketDispatcher.sendToServer(new ActivateSkillPacket(this));
					ticksTilFail = 0;
					return true;
				} else {
					ticksTilFail = 6;
				}
			} else if (key != mc.gameSettings.keyBindBack) { // activate on first press, but not for vanilla key!
				PacketDispatcher.sendToServer(new ActivateSkillPacket(this));
				return true;
			}
		}
		return false;
	}

	@Override
	protected boolean onActivated(World world, EntityPlayer player) {
		parryTimer = getActiveTime();
		attacksParried = 0;
		playMissSound = true;
		player.swingItem();
		return isActive();
	}

	@Override
	protected void onDeactivated(World world, EntityPlayer player) {
		parryTimer = 0;
	}

	@Override
	public void onUpdate(EntityPlayer player) {
		if (isActive()) {
			if (--parryTimer <= getParryDelay() && playMissSound) {
				playMissSound = false;
				PlayerUtils.playSoundAtEntity(player.worldObj, player, ModInfo.SOUND_SWORDMISS, 0.4F, 0.5F);
			}
		} else if (player.worldObj.isRemote && ticksTilFail > 0) {
			--ticksTilFail;
		}
	}

	@Override
	public boolean onBeingAttacked(EntityPlayer player, DamageSource source) {
		if (source.getEntity() instanceof EntityLivingBase) {
			EntityLivingBase attacker = (EntityLivingBase) source.getEntity();
			if (attacksParried < getMaxParries() && parryTimer > getParryDelay() && attacker.getHeldItem() != null && PlayerUtils.isHoldingSkillItem(player)) {
				if (player.worldObj.rand.nextFloat() < getDisarmChance(player, attacker)) {
					PlayerUtils.dropHeldItem(attacker);
				}
				++attacksParried; // increment after disarm
				PlayerUtils.playSoundAtEntity(player.worldObj, player, ModInfo.SOUND_SWORDSTRIKE, 0.4F, 0.5F);
				playMissSound = false;
				TargetUtils.knockTargetBack(attacker, player);
				return true;
			} // don't deactivate early, as there is a delay between uses
		}
		return false;
	}
}
