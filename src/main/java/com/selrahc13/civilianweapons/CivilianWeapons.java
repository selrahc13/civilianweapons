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
import com.selrahc13.civilianweapons.item.ItemHammer;
import com.selrahc13.civilianweapons.item.ItemNailBat;
import com.selrahc13.civilianweapons.item.ItemNails;
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
	public static Item baseballBat;
	public static Item nailBat;
	public static Item aluminumBat;
	public static Item tapedBat;
	public static Item tape;
	public static Item nails;
	public static Item hammer;
	
	// Materials
	public static ToolMaterial matWoodBat;
	public static ToolMaterial matAluminumBat;
	public static ToolMaterial matNailBat;
	public static ToolMaterial matTapedBat;
	
	// Register creative tab
	public static CreativeTabs tabCreative = new CreativeTabs(Reference.MODID + "_tabCivilianWeapons") {
		@Override
		public Item getTabIconItem() {
			return baseballBat;
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
		baseballBat = new ItemBaseballBat(matWoodBat);
		nailBat = new ItemNailBat(matNailBat);
		aluminumBat = new ItemAluminumBat(matAluminumBat);
		tapedBat = new ItemTapedBat(matTapedBat);
		tape = new ItemTape();
		nails = new ItemNails();
		hammer = new ItemHammer();
		
		logger.info("Registering items");
		RegisterHelper.RegisterItem(baseballBat);
		RegisterHelper.RegisterItem(tapedBat);
		RegisterHelper.RegisterItem(nailBat);
		RegisterHelper.RegisterItem(aluminumBat);
		RegisterHelper.RegisterItem(tape);
		RegisterHelper.RegisterItem(nails);
		RegisterHelper.RegisterItem(hammer);
		
		// Nailbat recipes
		GameRegistry.addShapelessRecipe(new ItemStack(nailBat), new Object[] {
			new ItemStack(baseballBat, 1, OreDictionary.WILDCARD_VALUE), 
			new ItemStack(tape, 1, OreDictionary.WILDCARD_VALUE), 
			nails, 
			new ItemStack(hammer, 1, OreDictionary.WILDCARD_VALUE)
		});
		
		GameRegistry.addShapelessRecipe(new ItemStack(nailBat), new Object[] {
			new ItemStack(tapedBat, 1, OreDictionary.WILDCARD_VALUE), 
			nails, 
			new ItemStack(hammer, 1, OreDictionary.WILDCARD_VALUE)
		});
		
		// Taped bat recipe
		GameRegistry.addShapelessRecipe(new ItemStack(tapedBat), new Object[] {
			new ItemStack(baseballBat, 1, OreDictionary.WILDCARD_VALUE), 
			new ItemStack(tape, 1, OreDictionary.WILDCARD_VALUE)
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

