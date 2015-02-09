//This File was created with the Minecraft-SMP Modelling Toolbox 2.0.0.0
// Copyright (C) 2015 Minecraft-SMP.de
// This file is for Flan's Flying Mod Version 4.0.x+

package com.selrahc13.civilianweapons.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import com.selrahc13.tmt.ModelRendererTurbo;

public class ModelBaseballBat extends ModelBase
{
	public ModelBaseballBat()
	{
		int textureX = 512;
		int textureY = 512;
		baseballbatModel = new ModelRendererTurbo[5];
		baseballbatModel[0] = new ModelRendererTurbo(this, 62, 1, textureX, textureY); // Box 0
		baseballbatModel[1] = new ModelRendererTurbo(this, 28, 1, textureX, textureY); // Box 1
		baseballbatModel[2] = new ModelRendererTurbo(this, 1, 0, textureX, textureY); // Box 2
		baseballbatModel[3] = new ModelRendererTurbo(this, 1, 19, textureX, textureY); // Box 3
		baseballbatModel[4] = new ModelRendererTurbo(this, 1, 9, textureX, textureY); // Box 4

		baseballbatModel[0].addBox(-50F, 8F, -8F, 8, 40, 8, 0F); // Box 0
		baseballbatModel[0].setRotationPoint(0F, -66F, 0F);

		baseballbatModel[1].addFlexBox(-50F, 8F, -8F, 8, 44, 8, 0F, -2.00F, -2.00F, -2.00F, -2.00F, ModelRendererTurbo.MR_BOTTOM); // Box 1
		baseballbatModel[1].setRotationPoint(0F, -26F, 0F);

		baseballbatModel[2].addShapeBox(-50F, 8F, -8F, 6, 3, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F); // Box 2
		baseballbatModel[2].setRotationPoint(1F, 17.5F, 1F);

		baseballbatModel[3].addShapeBox(-50F, 8F, -8F, 6, 3, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F); // Box 3
		baseballbatModel[3].setRotationPoint(1F, -69F, 1F);

		baseballbatModel[4].addShapeBox(-50F, 8F, -8F, 6, 2, 6, 0F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Box 4
		baseballbatModel[4].setRotationPoint(1F, 20.5F, 1F);

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		for(int i = 0; i < 5; i++)
		{
			baseballbatModel[i].render(f5);
		}
	}

	public void render(float f) {
		this.render(null, 0f, 0f, 0f, 0f, 0f, f);
	}
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	{
	}
	
	public ModelRendererTurbo baseballbatModel[];
}