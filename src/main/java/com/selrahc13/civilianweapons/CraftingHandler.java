/**
 * 
 */
package com.selrahc13.civilianweapons;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

import net.minecraft.item.ItemStack;

/**
 * @author selrahc13
 *
 */
public class CraftingHandler {
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) {
		for (int i = 0; i < event.craftMatrix.getSizeInventory(); i++) {
			if (event.craftMatrix.getStackInSlot(i) != null) {
				ItemStack item = event.craftMatrix.getStackInSlot(i);
				
				if (item != null) {
					ItemStack k = null;
					// Damageable tools used for crafting
					if (item.getItem() == CivilianWeapons.hammer) {
						k = new ItemStack(CivilianWeapons.hammer,2,(item.getItemDamage() + 1));
					} else if (item.getItem() == CivilianWeapons.tape) {
						k = new ItemStack(CivilianWeapons.tape,2,(item.getItemDamage() + 1));
						
					// Keep original damage amounts on crafted weapons
					} else if (item.getItem() == CivilianWeapons.baseballBat || item.getItem() == CivilianWeapons.tapedBat) {
						event.crafting.setItemDamage(item.getItemDamage());
						continue;
					}
					
					if (k == null) {
						continue;
						
					// Damage our damageable tools
					} else if (k.getItemDamage() >= k.getMaxDamage()) {
						k.stackSize--;
					}
					
					event.craftMatrix.setInventorySlotContents(i, k);
				}
			}
		}
	}
}
