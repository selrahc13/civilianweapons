package com.selrahc13.civilianweapons.util;
import com.selrahc13.civilianweapons.CivilianWeapons;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class RegisterHelper {
	public static void RegisterBlock (Block block) {
		GameRegistry.registerBlock(block, block.getUnlocalizedName().substring(5));
	}

	public static void RegisterItem (Item item) {
		CivilianWeapons.logger.info("Registering item: "+ item.getUnlocalizedName());
		GameRegistry.registerItem(item, item.getUnlocalizedName().substring(5));
	}
	
}
