package com.selrahc13.civilianweapons.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.Entity;

import com.selrahc13.tmt.ModelRendererTurbo;

public class ModelNailBat extends ModelBase
{
	public ModelNailBat()
	{
		int textureX = 512;
		int textureY = 512;
		nailbatModel = new ModelRendererTurbo[17];
		nailbatModel[0] = new ModelRendererTurbo(this, 62, 1, textureX, textureY); // Bat 0
		nailbatModel[1] = new ModelRendererTurbo(this, 28, 1, textureX, textureY); // Bat 1
		nailbatModel[2] = new ModelRendererTurbo(this, 1, 0, textureX, textureY); // Bat 2
		nailbatModel[3] = new ModelRendererTurbo(this, 1, 19, textureX, textureY); // Bat 4
		nailbatModel[4] = new ModelRendererTurbo(this, 1, 9, textureX, textureY); // Bat 5
		nailbatModel[5] = new ModelRendererTurbo(this, 3, 58, textureX, textureY); // Nail 6
		nailbatModel[6] = new ModelRendererTurbo(this, 3, 38, textureX, textureY); // Nail 7
		nailbatModel[7] = new ModelRendererTurbo(this, 3, 58, textureX, textureY); // Nail 8
		nailbatModel[8] = new ModelRendererTurbo(this, 3, 58, textureX, textureY); // Nail 9
		nailbatModel[9] = new ModelRendererTurbo(this, 3, 58, textureX, textureY); // Nail 10
		nailbatModel[10] = new ModelRendererTurbo(this, 3, 58, textureX, textureY); // Nail 11
		nailbatModel[11] = new ModelRendererTurbo(this, 3, 58, textureX, textureY); // Nail 12
		nailbatModel[12] = new ModelRendererTurbo(this, 3, 38, textureX, textureY); // Nail 13
		nailbatModel[13] = new ModelRendererTurbo(this, 3, 38, textureX, textureY); // Nail 13
		nailbatModel[14] = new ModelRendererTurbo(this, 3, 38, textureX, textureY); // Nail 14
		nailbatModel[15] = new ModelRendererTurbo(this, 3, 38, textureX, textureY); // Nail 15
		nailbatModel[16] = new ModelRendererTurbo(this, 3, 38, textureX, textureY); // Nail 17

		nailbatModel[0].addBox(-50F, 8F, -8F, 8, 40, 8, 0F); // Bat 0
		nailbatModel[0].setRotationPoint(-5F, -74F, -4F);

		nailbatModel[1].addFlexBox(-50F, 8F, -8F, 8, 44, 8, 0F, -2.00F, -2.00F, -2.00F, -2.00F, ModelRendererTurbo.MR_BOTTOM); // Bat 1
		nailbatModel[1].setRotationPoint(-5F, -34F, -4F);

		nailbatModel[2].addShapeBox(-50F, 8F, -8F, 6, 3, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F); // Bat 2
		nailbatModel[2].setRotationPoint(-4F, 9.5F, -3F);

		nailbatModel[3].addShapeBox(-50F, 8F, -8F, 6, 3, 6, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F); // Bat 4
		nailbatModel[3].setRotationPoint(-4F, -77F, -3F);

		nailbatModel[4].addShapeBox(-50F, 8F, -8F, 6, 2, 6, 0F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F, 1F, 0F, 1F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F, 0F); // Bat 5
		nailbatModel[4].setRotationPoint(-4F, 12.5F, -3F);

		nailbatModel[5].addBox(-57F, 8F, -9F, 17, 1, 1, 0F); // Nail 6
		nailbatModel[5].setRotationPoint(-6F, -66F, 0F);

		nailbatModel[6].addBox(-43F, 8F, -24F, 1, 1, 17, 0F); // Nail 7
		nailbatModel[6].setRotationPoint(-6F, -70F, 4F);

		nailbatModel[7].addBox(-57F, 8F, -26F, 17, 1, 1, 0F); // Nail 8
		nailbatModel[7].setRotationPoint(-6F, -72F, 19F);

		nailbatModel[8].addBox(-45F, 8F, -11F, 17, 1, 1, 0F); // Nail 9
		nailbatModel[8].setRotationPoint(-11F, -68F, 4F);

		nailbatModel[9].addBox(-49F, 8F, -18F, 17, 1, 1, 0F); // Nail 10
		nailbatModel[9].setRotationPoint(-7F, -74F, 9F);

		nailbatModel[10].addBox(-58F, 8F, -23F, 17, 1, 1, 0F); // Nail 11
		nailbatModel[10].setRotationPoint(2F, -71F, 12F);

		nailbatModel[11].addBox(-61F, 8F, -16F, 17, 1, 1, 0F); // Nail 12
		nailbatModel[11].setRotationPoint(-2F, -69F, 5F);

		nailbatModel[12].addBox(-56F, 8F, -18F, 1, 1, 17, 0F); // Nail 13
		nailbatModel[12].setRotationPoint(3F, -73F, 5F);

		nailbatModel[13].addBox(-48F, 8F, -24F, 1, 1, 17, 0F); // Nail 13
		nailbatModel[13].setRotationPoint(-6F, -67F, 4F);

		nailbatModel[14].addBox(-45F, 8F, -24F, 1, 1, 17, 0F); // Nail 14
		nailbatModel[14].setRotationPoint(-6F, -73F, 4F);

		nailbatModel[15].addBox(-54F, 8F, -18F, 1, 1, 17, 0F); // Nail 15
		nailbatModel[15].setRotationPoint(3F, -70F, 5F);

		nailbatModel[16].addBox(-52F, 8F, -18F, 1, 1, 17, 0F); // Nail 17
		nailbatModel[16].setRotationPoint(3F, -67F, 5F);


	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		for(int i = 0; i < 13; i++)
		{
			nailbatModel[i].render(f5);
		}
	}
	public void render(float f) {
		this.render(null, 0f, 0f, 0f, 0f, 0f, f);
	}

	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
	{
	}

	public ModelRendererTurbo nailbatModel[];
}