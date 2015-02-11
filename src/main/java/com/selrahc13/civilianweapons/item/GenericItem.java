/**
 * 
 */
package com.selrahc13.civilianweapons.item;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.selrahc13.civilianweapons.CivilianWeapons;
import com.selrahc13.civilianweapons.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author selrahc13
 *
 */
public class GenericItem extends Item{

	public GenericItem() {
		super();
		this.setCreativeTab(CivilianWeapons.tabCreative);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons (IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon(Reference.MODID + ":" + this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".")+1));
	}
}
