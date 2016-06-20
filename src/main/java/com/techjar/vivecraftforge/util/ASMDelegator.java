package com.techjar.vivecraftforge.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

public class ASMDelegator {
	private ASMDelegator() {
	}
	
	public static void scalePlayer(Entity entity) {
		if (entity instanceof EntityPlayer && Util.isVRPlayer((EntityPlayer)entity)) {
			float scale = Util.getVRPlayerScale((EntityPlayer)entity);
			//scale += scale * 0.1F;
			GL11.glScalef(scale, scale, scale);
		}
	}
}
