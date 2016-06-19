package com.techjar.vivecraftforge.client.render.entity;

import net.minecraft.client.model.ModelBiped;

import com.techjar.vivecraftforge.client.render.model.ModelVRHead;
import com.techjar.vivecraftforge.entity.EntityVRObject;
import com.techjar.vivecraftforge.util.Vector3;

public class RenderEntityVRHead extends RenderEntityVRObject {
	public RenderEntityVRHead() {
		super();
		model = new ModelVRHead();
		armorSlot = 3;
	}

	@Override
	public Vector3 getArmorModelOffset(EntityVRObject entity) {
		return new Vector3(0, 4F / 16F, 0);
	}

	@Override
	public void renderArmorModel(EntityVRObject entity, ModelBiped modelBiped, float scale) {
		modelBiped.bipedHead.render(scale);
		modelBiped.bipedHeadwear.render(scale);
	}
}
