package com.techjar.vivecraftforge.client.render.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelVRArm extends ModelBase {
	public ModelRenderer arm;
	
	public ModelVRArm(boolean isLeftArm) {
		this(0, isLeftArm);
	}
	
	public ModelVRArm(float scaleFactor, boolean isLeftArm) {
        arm = new ModelRenderer(this, 40, 16);
        arm.mirror = isLeftArm;
        arm.addBox(-2.0F, -6.0F, -2.0F, 4, 12, 4, scaleFactor);
        arm.setRotationPoint(0, 0, 0);
	}

	/**
	 * lol coords are not used here so don't even try
	 */
	@Override
	public void render(Entity entity, float x, float y, float z, float yaw, float pitch, float whatIsThis) {
		arm.render(whatIsThis);
	}
}
