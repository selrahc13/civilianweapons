package com.selrahc13.civilianweapons.item;

import mods.battlegear2.api.IOffhandDual;
import cpw.mods.fml.common.Optional;
import dynamicswordskills.api.ISword;

@Optional.InterfaceList({
    @Optional.Interface(modid = "battlegear2", iface = "mods.battlegear2.api.IOffhandDual"),
    @Optional.Interface(modid = "dynamicswordskills", iface = "dynamicswordskills.api.ISword")
})
public class ItemNailBat extends ItemBaseballBat implements ISword, IOffhandDual {
	public static boolean keyPressed = false;
	public static boolean keyHasBeenPressed = false;
	protected final ToolMaterial material;
	
	public ItemNailBat(ToolMaterial nailBatMaterial) {
		super(nailBatMaterial);
		this.material = nailBatMaterial;
		this.setUnlocalizedName("nailbat");
		this.setMaxDamage(250);
		setup();
	}
}