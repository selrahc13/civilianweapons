/**
 * 
 */
package com.selrahc13.civilianweapons.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import com.selrahc13.civilianweapons.CivilianWeapons;
import com.selrahc13.civilianweapons.Reference;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author selrahc13
 *
 */
@SuppressWarnings("unused")
public class GenericFood extends ItemFood {

    /** Number of ticks to run while 'EnumAction'ing until result. */
    public final int itemUseDuration;
    /** The amount this food item heals the player. */
	private final int healAmount;
    private final float saturationModifier;
    /** Whether wolves like this food (true for raw and cooked porkchop). */
    private final boolean isWolfsFavoriteMeat;
	private int potionId;
	private int potionDuration;
	private int potionAmplifier;
	private float potionEffectProbability;

	public GenericFood(int heal, float saturation, boolean wolfFavorite) {
		super(heal, saturation, wolfFavorite);
        this.itemUseDuration = 32;
        this.healAmount = heal;
        this.isWolfsFavoriteMeat = wolfFavorite;
        this.saturationModifier = saturation;
		this.setCreativeTab(CivilianWeapons.tabCreative);
	}
	
	public GenericFood(int heal, boolean wolfFavorite) {
		this(heal, 0.6f, wolfFavorite);
	}

    public ItemStack onEaten(ItemStack theFood, World theWorld, EntityPlayer thePlayer)
    {
    	// If there is food left in the container, eat it
    	if (theFood.getMaxDamage() > 0 && theFood.getItemDamage() + 1 < theFood.getMaxDamage()) {
    		theFood.setItemDamage(theFood.getItemDamage() + 1);		
    	} else if(theFood.getUnlocalizedName().contains("canned")) {
    		// Give back an empty container
			thePlayer.inventory.setInventorySlotContents(
				thePlayer.inventory.currentItem, 
				new ItemStack(CivilianWeapons.itemEmptyCan)
			);
		} else {
   			// Deplete the food source
   			--theFood.stackSize;
    	}
        thePlayer.getFoodStats().func_151686_a(this, theFood);
        theWorld.playSoundAtEntity(thePlayer, "random.burp", 0.5F, theWorld.rand.nextFloat() * 0.1F + 0.9F);
        this.onFoodEaten(theFood, theWorld, thePlayer);        
        return theFood;
    }

    protected void onFoodEaten(ItemStack theFood, World theWorld, EntityPlayer thePlayer)
    {
        if (!theWorld.isRemote && this.potionId > 0 && theWorld.rand.nextFloat() < this.potionEffectProbability)
        {
            thePlayer.addPotionEffect(new PotionEffect(this.potionId, this.potionDuration * 20, this.potionAmplifier));
        }
    }
	
	@SideOnly(Side.CLIENT)
	public void registerIcons (IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon(Reference.MODID + ":" + this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".")+1));
	}
}
