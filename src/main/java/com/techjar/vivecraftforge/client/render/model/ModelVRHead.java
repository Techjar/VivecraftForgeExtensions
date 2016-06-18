package com.techjar.vivecraftforge.client.render.model;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;

public class ModelVRHead extends ModelBase {
	public ModelRenderer head;
	public ModelRenderer headwear;
	
	public ModelVRHead() {
		this(0);
	}
	
	public ModelVRHead(float scaleFactor) {
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, scaleFactor);
		head.setRotationPoint(0, 0, 0);
		headwear = new ModelRenderer(this, 32, 0);
		headwear.addBox(-4.0F, -4.0F, -4.0F, 8, 8, 8, scaleFactor + 0.5F);
		headwear.setRotationPoint(0, 0, 0);
	}

	/**
	 * lol coords are not used here so don't even try
	 */
	@Override
	public void render(Entity entity, float x, float y, float z, float pitch, float yaw, float whatIsThis) {
		head.render(whatIsThis);
		headwear.render(whatIsThis);
	}
}
