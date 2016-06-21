package com.techjar.vivecraftforge.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ASMDelegator {
	private ASMDelegator() {
	}
	
	@SideOnly(Side.CLIENT)
	public static void scalePlayer(Entity entity) {
		if (entity instanceof EntityPlayer && Util.isVRPlayer((EntityPlayer)entity)) {
			float scale = Util.getVRPlayerScale((EntityPlayer)entity);
			GL11.glScalef(scale, scale, scale);
		}
	}
	
	public static double playerEntityReachDistance(EntityPlayer player, double originalValue) {
		return originalValue < 100 * 100 && Util.isVRPlayerServer(player) ? 100 * 100 : originalValue;
	}
	
	public static double playerBlockReachDistance(EntityPlayer player, double originalValue) {
		return originalValue < 100 && Util.isVRPlayerServer(player) ? 100 : originalValue;
	}
}
