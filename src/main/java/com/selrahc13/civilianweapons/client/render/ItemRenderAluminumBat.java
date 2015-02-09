package com.selrahc13.civilianweapons.client.render;

import org.lwjgl.opengl.GL11;

import com.selrahc13.civilianweapons.CivilianWeapons;
import com.selrahc13.civilianweapons.Reference;
import com.selrahc13.civilianweapons.client.model.ModelBaseballBat;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class ItemRenderAluminumBat implements IItemRenderer {

	protected static ModelBaseballBat model = new ModelBaseballBat();
	protected float scale;
	protected float rescale;
	
	public ItemRenderAluminumBat() {
		scale = 1.0f;
		rescale = scale;
		CivilianWeapons.logger.info("Registering ItemRendererAluminumBat");
	}
	
	public ItemRenderAluminumBat(float f) {
		scale = f;
		rescale = scale;
		CivilianWeapons.logger.info("Registering ItemRendererAluminumBat");
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        switch(type){
	        case EQUIPPED: return true;
	        case EQUIPPED_FIRST_PERSON: return true;
	        default: return false;
	    }
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,
			ItemRendererHelper helper) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type) {
	       case EQUIPPED: // render in third person
		        GL11.glPushMatrix(); //start gl rendering for this section
	    	   	GL11.glScalef(scale, scale, scale);
		        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(
		        		Reference.MODID.toLowerCase(),"textures/items/SkinAluminumBat.png"));
		        GL11.glRotatef(0F, 1.0f, 0.0f, 0.0f);	   //rotate 0 ° on X axis
		        GL11.glRotatef(-5F, 0.0f, 1.0f, 0.0f);	  // rotate -5 ° on Y axis
		        GL11.glRotatef(-150F, 0.0f, 0.0f, 1.0f);  // rotate -150 ° on Z axis
		        GL11.glTranslatef(0F, 0F, 0F);    //translate model to fit in the hand of the player
		//the entity argument can/could be passed to as null.
		        model.render((Entity) data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F,
                0.0625F);
        GL11.glPopMatrix(); //Stop gl rendering for this section
        break;

    case EQUIPPED_FIRST_PERSON:
//rinse and repeat the rendering. adjust axis' and translation as needed
        GL11.glPushMatrix();
        GL11.glTranslatef(0F, 0F, 0F);
        GL11.glScalef(scale, scale, scale);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(
        		Reference.MODID.toLowerCase(),"textures/items/SkinAluminumBat.png"));
        GL11.glRotatef(0F, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(-5F, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-150F, 0.0f, 0.0f, 1.0f);
        model.render((Entity) data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F,
                0.0625F);
        GL11.glPopMatrix();
        break;

    case ENTITY:
        GL11.glPushMatrix();
        rescale = 1.5F * scale;
        GL11.glScalef(rescale, rescale, rescale);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(
                Reference.MODID.toLowerCase(),"textures/items/SkinAluminumBat.png"));
        GL11.glRotatef(90F, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(0F, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(45F, 0.0f, 0.0f, 1.0f);
        GL11.glTranslatef(-0.2F, 1F, 0F);
        model.render((Entity) data[1], 0.0F, 0.0F, 0.0F, 0.0F, 0.0F,
                0.0625F);
        GL11.glPopMatrix();
        break;

    case INVENTORY:
        GL11.glPushMatrix();
        rescale = 0.7F * scale;
        GL11.glScalef(rescale, rescale, rescale);
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(
        		Reference.MODID.toLowerCase(),"textures/items/SkinAluminumBat.png"));

        GL11.glRotatef(200F, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(-80F, 0.0f, 1.0f, 0.0f);
        GL11.glTranslatef(0.0F, 1.2F, 0F);
//this is a method made by me in my model class to render only the modelparts, without an entity argument, because in your inventory, //the entity is always null.
        model.render(0.0625F);
        GL11.glPopMatrix();
        break;

    default:
        break;
    }		
	}

}
