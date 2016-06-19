package com.techjar.vivecraftforge.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

public class ASMDelegator {
	private ASMDelegator() {
	}
	
	public static void scalePlayer(Entity entity) {
		if (entity instanceof EntityPlayer && Util.isVRPlayer((EntityPlayer)entity)) {
			float height = Util.getVRPlayerHeadHeight((EntityPlayer)entity) - 0.25F;
			float scale = 1.62F / (height - (float)entity.posY);
			GL11.glScalef(scale, scale, scale);
		}
	}
}
