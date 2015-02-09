package com.selrahc13.civilianweapons.item;

import mods.battlegear2.api.IOffhandDual;
import cpw.mods.fml.common.Optional;
import dynamicswordskills.api.ISword;

@Optional.InterfaceList({
    @Optional.Interface(modid = "battlegear2", iface = "mods.battlegear2.api.IOffhandDual"),
    @Optional.Interface(modid = "dynamicswordskills", iface = "dynamicswordskills.api.ISword")
})
public class ItemAluminumBat extends ItemBaseballBat implements IOffhandDual, ISword {
	public static boolean keyPressed = false;
	public static boolean keyHasBeenPressed = false;
	protected final ToolMaterial material;
	
	public ItemAluminumBat(ToolMaterial baseballBatMaterial) {
		super(baseballBatMaterial);
		this.material = baseballBatMaterial;
		this.setUnlocalizedName("aluminumbat");
		this.setMaxDamage(600);
		setup();
	}
}