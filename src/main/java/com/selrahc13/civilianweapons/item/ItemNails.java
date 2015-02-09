package com.selrahc13.civilianweapons.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.selrahc13.civilianweapons.CivilianWeapons;
import com.selrahc13.civilianweapons.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemNails extends Item {
	public ItemNails() {
		super();
		this.setUnlocalizedName("nails");
		this.setMaxStackSize(64);
		this.setCreativeTab(CivilianWeapons.tabCreative);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons (IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon(Reference.MODID + ":" + this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".")+1));
	}
}
