/**
 * 
 */
package com.selrahc13.civilianweapons.item;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author selrahc13
 *
 */

public class HealingItem extends GenericItem {
	private float healAmount;
	
	/**
	 * @return the healAmount
	 */
	public float getHealAmount() {
		return healAmount;
	}

	/**
	 * @param healAmount the healAmount to set
	 */
	public void setHealAmount(float healAmount) {
		this.healAmount = healAmount;
	}

	public HealingItem() {
		super();
	}

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    public EnumAction getItemUseAction(ItemStack theItem)
    {
        return EnumAction.block;
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    // TODO: Add potion effects processing
    public ItemStack onItemRightClick(ItemStack theItem, World theWorld, EntityPlayer thePlayer)
    {
        thePlayer.setItemInUse(theItem, this.getMaxItemUseDuration(theItem));

        // If the item is multi-use, use one
    	if (theItem.getMaxDamage() > 0 && theItem.getItemDamage() + 1 < theItem.getMaxDamage()) {
    		theItem.setItemDamage(theItem.getItemDamage() + 1);		
			List<String> lore = new ArrayList<String>();
			lore.add(theItem.getMaxDamage() - theItem.getItemDamage() + " of " + theItem.getMaxDamage() + " uses remaining");
			theItem.getItem().addInformation(theItem, thePlayer, lore, false);
		} else {
   			// Deplete the food source
   			--theItem.stackSize;
    	}
    	thePlayer.heal(healAmount);
        //thePlayer.getFoodStats().func_151686_a(this, theItem);
        theWorld.playSoundAtEntity(thePlayer, "note.pling", 0.5F, theWorld.rand.nextFloat() * 0.1F + 0.9F);
        //this.onFoodEaten(theItem, theWorld, thePlayer);        
        return theItem;
    }

}
