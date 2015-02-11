package com.selrahc13.civilianweapons;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.logging.log4j.Logger;

import com.selrahc13.civilianweapons.common.CommonProxy;
import com.selrahc13.civilianweapons.item.ItemAluminumBat;
import com.selrahc13.civilianweapons.item.ItemBaseballBat;
import com.selrahc13.civilianweapons.item.ItemBatteries;
import com.selrahc13.civilianweapons.item.ItemBeans;
import com.selrahc13.civilianweapons.item.ItemEmptyCan;
import com.selrahc13.civilianweapons.item.ItemFirstAid;
import com.selrahc13.civilianweapons.item.ItemHammer;
import com.selrahc13.civilianweapons.item.ItemNailBat;
import com.selrahc13.civilianweapons.item.ItemNails;
import com.selrahc13.civilianweapons.item.ItemSoup;
import com.selrahc13.civilianweapons.item.ItemTape;
import com.selrahc13.civilianweapons.item.ItemTapedBat;
import com.selrahc13.civilianweapons.util.RegisterHelper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler; // used in 1.6.2
//import cpw.mods.fml.common.Mod.PreInit;    // used in 1.5.2
//import cpw.mods.fml.common.Mod.Init;       // used in 1.5.2
//import cpw.mods.fml.common.Mod.PostInit;   // used in 1.5.2
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
//import cpw.mods.fml.common.network.NetworkMod; // not used in 1.7
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(	modid=Reference.MODID, 
		name=Reference.MODNAME, 
		version=Reference.MODVERSION,         
		dependencies = Reference.DEPENDENCIES
)
public class CivilianWeapons {
	@Instance(Reference.MODID)
	public static CivilianWeapons instance;	
	
	public static Logger logger;

	// Items
	public static Item itemBaseballBat;
	public static Item itemNailBat;
	public static Item itemAluminumBat;
	public static Item itemTapedBat;
	public static Item itemTape;
	public static Item itemNails;
	public static Item itemHammer;
	public static Item itemEmptyCan;
	public static Item itemSoup;
	public static Item itemBeans;
	public static Item itemBatteries;
	public static Item itemFirstAid;
	
	// Materials
	public static ToolMaterial matWoodBat;
	public static ToolMaterial matAluminumBat;
	public static ToolMaterial matNailBat;
	public static ToolMaterial matTapedBat;
	
	// Register creative tab
	public static CreativeTabs tabCreative = new CreativeTabs(Reference.MODID + "_tabCivilianWeapons") {
		@Override
		public Item getTabIconItem() {
			return itemTapedBat;
		}
	};

	@SidedProxy(serverSide="com.selrahc13.civilianweapons.common.CommonProxy", clientSide="com.selrahc13.civilianweapons.client.ClientProxy") 
	public static CommonProxy proxy; 

	@EventHandler	// used in 1.6.2
	//@PreInit		// used in 1.5.2
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
		// Register new materials
		matWoodBat = EnumHelper.addToolMaterial("matWoodBat", 0, 100, 15.0F, 4.0F, 0);
		matNailBat = EnumHelper.addToolMaterial("matNailBat", 0, 200, 15.0F, 8.0F, 0);
		matAluminumBat = EnumHelper.addToolMaterial("matAluminumBat", 0, 600, 15.0F, 5.0F, 0);
		matTapedBat = EnumHelper.addToolMaterial("matTapedBat", 0, 150, 15.f, 5.5F, 0);
		
		// Register new items
		itemBaseballBat = new ItemBaseballBat(matWoodBat);
		itemNailBat = new ItemNailBat(matNailBat);
		itemAluminumBat = new ItemAluminumBat(matAluminumBat);
		itemTapedBat = new ItemTapedBat(matTapedBat);
		itemTape = new ItemTape();
		itemNails = new ItemNails();
		itemHammer = new ItemHammer();
		itemBatteries = new ItemBatteries();
		itemSoup = new ItemSoup();
		itemBeans = new ItemBeans();
		itemEmptyCan = new ItemEmptyCan();
		itemFirstAid = new ItemFirstAid();
		
		logger.info("Registering items");
		RegisterHelper.RegisterItem(itemBaseballBat);
		RegisterHelper.RegisterItem(itemTapedBat);
		RegisterHelper.RegisterItem(itemNailBat);
		RegisterHelper.RegisterItem(itemAluminumBat);
		RegisterHelper.RegisterItem(itemTape);
		RegisterHelper.RegisterItem(itemNails);
		RegisterHelper.RegisterItem(itemHammer);
		RegisterHelper.RegisterItem(itemBatteries);
		RegisterHelper.RegisterItem(itemSoup);
		RegisterHelper.RegisterItem(itemBeans);
		RegisterHelper.RegisterItem(itemEmptyCan);
		RegisterHelper.RegisterItem(itemFirstAid);
		
		
		// Nailbat recipes
		GameRegistry.addShapelessRecipe(new ItemStack(itemNailBat), new Object[] {
			new ItemStack(itemTapedBat, 1, OreDictionary.WILDCARD_VALUE), 
			new ItemStack(itemTape, 1, OreDictionary.WILDCARD_VALUE), 
			itemNails, 
			new ItemStack(itemHammer, 1, OreDictionary.WILDCARD_VALUE)
		});
		
		GameRegistry.addShapelessRecipe(new ItemStack(itemNailBat), new Object[] {
			new ItemStack(itemTapedBat, 1, OreDictionary.WILDCARD_VALUE), 
			itemNails, 
			new ItemStack(itemHammer, 1, OreDictionary.WILDCARD_VALUE)
		});
		
		// Taped bat recipe
		GameRegistry.addShapelessRecipe(new ItemStack(itemTapedBat), new Object[] {
			new ItemStack(itemTapedBat, 1, OreDictionary.WILDCARD_VALUE), 
			new ItemStack(itemTape, 1, OreDictionary.WILDCARD_VALUE)
		});
	}

	@EventHandler	// used in 1.6.2
	//@Init			// used in 1.5.2
	public void load(FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new CraftingHandler());
		proxy.registerRenderers();
	}
	
	@EventHandler	// used in 1.6.2
	//@PostInit		// used in 1.5.2
	public void postInit(FMLPostInitializationEvent event) {
		// Stub method
	}	
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
	}
}

