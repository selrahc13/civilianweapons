package com.selrahc13.civilianweapons.item;

import java.util.List;

import com.selrahc13.civilianweapons.CivilianWeapons;
import com.selrahc13.civilianweapons.Reference;





/*
import mods.battlegear2.api.PlayerEventChild;
import mods.battlegear2.api.PlayerEventChild.OffhandAttackEvent;
import mods.battlegear2.api.weapons.IBattlegearWeapon;
*/
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.Optional.Method;
import dynamicswordskills.api.ISword;
import mods.battlegear2.api.IOffhandDual;
import mods.battlegear2.api.PlayerEventChild.OffhandAttackEvent;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

@Optional.InterfaceList({
    @Optional.Interface(modid = "battlegear2", iface = "mods.battlegear2.api.IOffhandDual"),
    @Optional.Interface(modid = "dynamicswordskills", iface = "dynamicswordskills.api.ISword")
})
public class ItemBaseballBat extends ItemSword implements IOffhandDual, ISword{
	public static boolean keyPressed = false;
	public static boolean keyHasBeenPressed = false;
	protected final ToolMaterial material;
	
	public ItemBaseballBat(ToolMaterial baseballBatMaterial) {
		super(baseballBatMaterial);
		this.material = baseballBatMaterial;
		this.setUnlocalizedName("baseballbat");
		this.setMaxDamage(100);
		setup();
	}

	public void setup() {
		this.setCreativeTab(CivilianWeapons.tabCreative);
		this.setMaxStackSize(1);
		this.setFull3D();
		this.setNoRepair();
		CivilianWeapons.logger.info("Adding baseball bat: " + this.getUnlocalizedName());		
	}
	
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon(Reference.MODID + ":" + this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(".")+1));
	}
	
	public ToolMaterial getMaterial() {
		return this.material;
	}
	
    @Override
    @SideOnly(Side.CLIENT)
    public EnumRarity getRarity(ItemStack par1ItemStack){
        return EnumRarity.epic;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, EntityPlayer player, List dataList, boolean bool){
    	dataList.add("Knock one out of the park");
    }

    /*---- Battlegear Support START ----*/
	@Override
    @Method(modid = "battlegear2")
	public boolean isOffhandHandDual(ItemStack off) {
		return false;
	}

	@Override
    @Method(modid = "battlegear2")
	public boolean offhandAttackEntity(OffhandAttackEvent event,
			ItemStack mainhandItem, ItemStack offhandItem) {
		return false;
	}

	@Override
    @Method(modid = "battlegear2")
	public boolean offhandClickAir(PlayerInteractEvent event,
			ItemStack mainhandItem, ItemStack offhandItem) {
		return false;
	}

	@Override
    @Method(modid = "battlegear2")
	public boolean offhandClickBlock(PlayerInteractEvent event,
			ItemStack mainhandItem, ItemStack offhandItem) {
		return false;
	}

	@Override
    @Method(modid = "battlegear2")
	public void performPassiveEffects(Side effectiveSide,
			ItemStack mainhandItem, ItemStack offhandItem) {
		
	}

    /*---- Battlegear Support END ----*/
}