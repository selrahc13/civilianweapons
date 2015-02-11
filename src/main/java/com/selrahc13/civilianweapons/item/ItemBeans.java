/**
 * 
 */
package com.selrahc13.civilianweapons.item;

/**
 * @author selrahc13
 *
 */
public class ItemBeans extends GenericFood {
	public ItemBeans(int heal, float saturation, boolean wolfFavorite) {
		super(heal, saturation, wolfFavorite);
		this.setUnlocalizedName("cannedbeans");
		this.setMaxStackSize(1);
		this.setMaxDamage(4);
		this.setNoRepair();
	}
	
	public ItemBeans(int heal) {
		this(heal, 0.6f, false);
	}
	
	public ItemBeans() {
		this(2);
	}
}
