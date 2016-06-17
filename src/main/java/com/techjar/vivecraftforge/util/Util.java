package com.techjar.vivecraftforge.util;

import org.lwjgl.util.vector.Vector3f;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Util {
	private Util() {
	}
	
	@SideOnly(Side.CLIENT)
	public static void setVectorFromVec(Vector3f vector, Vec3 vec) {
		vector.set((float)vec.xCoord, (float)vec.yCoord, (float)vec.zCoord);
	}
	
	public static boolean isVRPlayer(EntityPlayer entity) {
		return false; // TODO
	}
	
	public static float getVRPlayerHeadHeight(EntityPlayer entity) {
		return (float)entity.posY + 1.62F; // TODO
	}
}
