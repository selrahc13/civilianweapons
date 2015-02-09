package com.selrahc13.civilianweapons.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import com.selrahc13.civilianweapons.CivilianWeapons;
import com.selrahc13.civilianweapons.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemHammer extends Item {
	public ItemHammer() {
		super();
		this.setUnlocalizedName("hammer");
		this.setMaxStackSize(1);
		this.setMaxDamage(64);
		this.setNoRepair();
		this.setCreativeTab(CivilianWeapons.tabCreative);
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons (IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon(Reference.MODID + ":" + this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".")+1));
	}
}
