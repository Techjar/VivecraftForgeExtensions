package com.techjar.vivecraftforge.client.render.entity;

import com.techjar.vivecraftforge.client.render.model.ModelVRHead;

public class RenderEntityVRHead extends RenderEntityVRObject {
	public RenderEntityVRHead() {
		super();
		model = new ModelVRHead();
		armorSlot = 3;
	}
}
