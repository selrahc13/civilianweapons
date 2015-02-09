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

package dynamicswordskills.api;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.google.common.collect.Multimap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import dynamicswordskills.skills.SkillBase;

/**
 * 
 * Base class providing all the functionality needed for ISkillItem as well as
 * acting almost exactly like a regular sword (aside from the web-cutting ability),
 * without actually being a sword.
 * 
 * If the item cannot for some reason extend {@link #ItemSword} but should still be
 * considered a sword, {@link ISword} should also be implemented. This will allow
 * the item to be used with skills that require swords, such as Mortal Draw.
 * 
 * Note that an ISkillProvider item will always be able to use the skill it provides,
 * even if the item is not a sword and the skill would otherwise require such.
 * 
 * It is a simple class, having a set skill and level per item rather than using NBT.
 * 
 * Easily create a new skill-providing weapon simply by extending this class.
 *
 */
public class ItemSkillProvider extends Item implements ISkillProvider
{
	/** The weapon's tool material determines damage and durability */
	private final ToolMaterial material;

	/** Weapon damage is based on tool material, just like swords */
	private float weaponDamage;

	/** The skill id and level of the SkillBase.{skill} granted by this Item */
	private final byte skillId, level;

	/** This is used mainly for the tooltip display */
	private SkillBase skill;

	/** Whether the player should be granted basic sword skill should they not have it */
	private final boolean grantsBasicSkill;

	/**
	 * Shortcut method sets ISkillItem to always grant Basic Sword skill if needed to use
	 * the main skill designated by the skill id below.
	 * Standard sword-like weapon with max stack size of 1; be sure to set the unlocalized
	 * name, texture, and creative tab using chained methods if using the class as is.
	 * @param material	the tool material determines both durability and damage
	 * @param skill		use SkillBase.{skill} during construction to ensure a valid skill
	 * @param level		should be at least 1, and will be capped automatically at the skill's max level
	 */
	public ItemSkillProvider(ToolMaterial material, SkillBase skill, byte level) {
		this(material, skill, level, true);
	}

	/**
	 * Standard sword-like weapon with max stack size of 1; be sure to set the unlocalized
	 * name, texture, and creative tab using chained methods if using the class as is.
	 * @param material			the tool material determines both durability and damage
	 * @param skill				use SkillBase.{skill} during construction to ensure a valid skill
	 * @param level				should be at least 1, and will be capped automatically at the skill's max level
	 * @param grantsBasicSkill	if true, the player will be temporarily granted Basic Sword skill in
	 * 							order to use the ISkillItem's main skill, if other than Basic Sword
	 */
	public ItemSkillProvider(ToolMaterial material, SkillBase skill, byte level, boolean grantsBasicSkill) {
		super();
		this.material = material;
		this.weaponDamage = 4.0F + this.material.getDamageVsEntity();
		this.skillId = skill.getId();
		this.level = level;
		this.grantsBasicSkill = grantsBasicSkill;
		setMaxDamage(this.material.getMaxUses());
		setMaxStackSize(1);
	}

	/**
	 * A convenience / optimizer for displaying item tooltips; never used by the rest of the API
	 * Store the leveled SkillBase locally in the Item class the first time the method is
	 * called to improve efficiency (since the level will never change) using the method
	 * {@link SkillBase#getSkillFromItem(ItemStack, ISkillProvider) SkillBase.getSkillFromItem}
	 * @param stack not used in this implementation, but required for the SkillBase method above
	 * @return	DO NOT use the returned skill as a player's active instance - it is not unique!
	 */
	protected SkillBase getSkill(ItemStack stack) {
		if (skill == null) {
			skill = SkillBase.getSkillFromItem(stack, this);
		}
		return skill;
	}

	@Override
	public int getSkillId(ItemStack stack) {
		return skillId;
	}

	@Override
	public byte getSkillLevel(ItemStack stack) {
		return level;
	}

	@Override
	public boolean grantsBasicSwordSkill(ItemStack stack) {
		return grantsBasicSkill;
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean isFull3D() {
		return true;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.block;
	}

	@Override
	public int getItemEnchantability() {
		return material.getEnchantability();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack stack) {
		return material.func_150995_f() == stack.getItem() ? true : super.getIsRepairable(toRepair, stack);
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, Block block, int x, int y, int z, EntityLivingBase entity) {
		if (block.getBlockHardness(world, x, y, z) != 0.0D) {
			stack.damageItem(2, entity);
		}
		return true;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		player.setItemInUse(stack, getMaxItemUseDuration(stack));
		return stack;
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		SkillBase skill = getSkill(stack);
		return StatCollector.translateToLocal("item.dss.skillitem.name") + (skill != null ? (" " + skill.getDisplayName()) : "");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack,	EntityPlayer player, List list, boolean par4) {
		SkillBase skill = getSkill(stack);
		if (skill != null) {
			list.add(StatCollector.translateToLocalFormatted("tooltip.dss.skillprovider.desc.skill", EnumChatFormatting.GOLD + skill.getDisplayName()));
			list.add(StatCollector.translateToLocalFormatted("tooltip.dss.skillprovider.desc.level", skill.getLevel(), skill.getMaxLevel()));
			if (grantsBasicSwordSkill(stack)) {
				list.add(StatCollector.translateToLocalFormatted("tooltip.dss.skillprovider.desc.provider", EnumChatFormatting.DARK_GREEN + SkillBase.swordBasic.getDisplayName()));
			}
			list.addAll(skill.getDescription(player));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Multimap getAttributeModifiers(ItemStack stack) {
		Multimap multimap = super.getAttributeModifiers(stack);
		multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(field_111210_e, "Weapon modifier", weaponDamage, 0));
		return multimap;
	}
}
