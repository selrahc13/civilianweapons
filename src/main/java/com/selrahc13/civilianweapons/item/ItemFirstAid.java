/**
 * 
 */
package com.selrahc13.civilianweapons.item;


/**
 * @author selrahc13
 *
 */
public class ItemFirstAid extends HealingItem {

	/**
	 * 
	 */
	public ItemFirstAid() {
		super();
		this.setMaxDamage(10);
		this.setMaxStackSize(1);
		this.setHealAmount(5);
		this.setUnlocalizedName("firstaid");
	}	
}
