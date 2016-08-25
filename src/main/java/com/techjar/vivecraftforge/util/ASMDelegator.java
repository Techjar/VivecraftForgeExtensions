package com.techjar.vivecraftforge.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import org.lwjgl.opengl.GL11;

import com.techjar.vivecraftforge.proxy.ProxyClient;
import com.techjar.vivecraftforge.proxy.ProxyServer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ASMDelegator {
	private ASMDelegator() {
	}
	
	@SideOnly(Side.CLIENT)
	public static void scalePlayer(Entity entity) {
		if (entity instanceof EntityPlayer && ProxyClient.isVRPlayer((EntityPlayer)entity)) {
			float scale = ProxyClient.getVRPlayerScale((EntityPlayer)entity);
			GL11.glScalef(scale, scale, scale);
		}
	}
	
	public static double playerEntityReachDistance(EntityPlayer player, double originalValue) {
		return originalValue < 256 * 256 && ProxyServer.isVRPlayer(player) ? 256 * 256 : originalValue;
	}
	
	public static double playerBlockReachDistance(EntityPlayer player, double originalValue) {
		return originalValue < 256 && ProxyServer.isVRPlayer(player) ? 256 : originalValue;
	}
	
	public static double creeperSwellDistance(double originalValue, EntityLivingBase entity) {
		if (entity == null || !(entity instanceof EntityPlayer)) return originalValue;
		if (ProxyServer.isVRPlayer((EntityPlayer)entity) && !ProxyServer.getVRPlayerSeated((EntityPlayer)entity)) return 1.75D * 1.75D;
		return originalValue;
	}
}
