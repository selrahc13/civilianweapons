package com.selrahc13.civilianweapons.client;

import net.minecraftforge.client.MinecraftForgeClient;

import com.selrahc13.civilianweapons.CivilianWeapons;
import com.selrahc13.civilianweapons.client.render.ItemRenderAluminumBat;
import com.selrahc13.civilianweapons.client.render.ItemRenderBaseballBat;
import com.selrahc13.civilianweapons.client.render.ItemRenderNailBat;
import com.selrahc13.civilianweapons.client.render.ItemRenderTapedBat;
import com.selrahc13.civilianweapons.common.CommonProxy;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderers() {
		// This is for rendering entities etc
		CivilianWeapons.logger.info("Registering renderers");
		MinecraftForgeClient.registerItemRenderer(CivilianWeapons.itemBaseballBat, new ItemRenderBaseballBat(0.25f));
		MinecraftForgeClient.registerItemRenderer(CivilianWeapons.itemTapedBat, new ItemRenderTapedBat(0.25f));
		MinecraftForgeClient.registerItemRenderer(CivilianWeapons.itemAluminumBat, new ItemRenderAluminumBat(0.25f));
		MinecraftForgeClient.registerItemRenderer(CivilianWeapons.itemNailBat, new ItemRenderNailBat(0.25f));
	}
	
}
