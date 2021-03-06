package com.selrahc13.civilianweapons.item;

import mods.battlegear2.api.IOffhandDual;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.Optional;
import dynamicswordskills.api.ISword;

@Optional.InterfaceList({
    @Optional.Interface(modid = "battlegear2", iface = "mods.battlegear2.api.IOffhandDual"),
    @Optional.Interface(modid = "dynamicswordskills", iface = "dynamicswordskills.api.ISword")
})
public class ItemTapedBat extends ItemBaseballBat implements ISword, IOffhandDual {
	public static boolean keyPressed = false;
	public static boolean keyHasBeenPressed = false;
	protected final ToolMaterial material;
	
	public ItemTapedBat(ToolMaterial baseballBatMaterial) {
		super(baseballBatMaterial);
		this.material = baseballBatMaterial;
		this.setUnlocalizedName("tapedbat");
		this.setMaxDamage(100);
		this.getAttributeModifiers(new ItemStack(this));
		setup();
	}
}