package com.techjar.vivecraftforge.client.render.entity;

import net.minecraft.client.model.ModelBiped;

import com.techjar.vivecraftforge.client.render.model.ModelVRArm;
import com.techjar.vivecraftforge.entity.EntityVRArm;
import com.techjar.vivecraftforge.entity.EntityVRObject;
import com.techjar.vivecraftforge.util.Vector3;

public class RenderEntityVROffHandArm extends RenderEntityVRObject {
	public RenderEntityVROffHandArm() {
		super();
		model = new ModelVRArm();
		armorSlot = 2;
	}

	@Override
	public Vector3 getArmorModelOffset(EntityVRObject entity) {
		return new Vector3((((EntityVRArm)entity).mirror ? 1F / 16F : -1F / 16F) - (5F / 16F), -12F / 16F, 0);
	}

	@Override
	public void renderArmorModel(EntityVRObject entity, ModelBiped modelBiped, float scale) {
		if (((EntityVRArm)entity).mirror) {
			modelBiped.bipedRightArm.render(scale);
		} else {
			modelBiped.bipedLeftArm.render(scale);
		}
	}

	@Override
	public void preRenderModel(EntityVRObject entity) {
		((ModelVRArm)model).mirror = !((EntityVRArm)entity).mirror;
	}
}
