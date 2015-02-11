/**
 * 
 */
package com.selrahc13.civilianweapons.item;

/**
 * @author selrahc13
 *
 */
public class ItemSoup extends GenericFood {
	public ItemSoup() {
		super(1, 0.6f, false);
		this.setUnlocalizedName("cannedsoup");
		this.setMaxStackSize(1);
		this.setMaxDamage(2);
	}
}
