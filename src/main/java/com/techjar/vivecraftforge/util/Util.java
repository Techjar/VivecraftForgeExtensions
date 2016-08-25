package com.techjar.vivecraftforge.util;

import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.techjar.vivecraftforge.entity.EntityVRObject;
import com.techjar.vivecraftforge.proxy.ProxyClient;
import com.techjar.vivecraftforge.proxy.ProxyServer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.Vec3;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class Util {
	private Util() {
	}

	@SideOnly(Side.CLIENT)
	public static Vector2f convertVector(Vector2 vector) {
		return new Vector2f(vector.getX(), vector.getY());
	}

	@SideOnly(Side.CLIENT)
	public static Vector2 convertVector(Vector2f vector) {
		return new Vector2(vector.getX(), vector.getY());
	}

	@SideOnly(Side.CLIENT)
	public static Vector3f convertVector(Vector3 vector) {
		return new Vector3f(vector.getX(), vector.getY(), vector.getZ());
	}

	@SideOnly(Side.CLIENT)
	public static Vector3 convertVector(Vector3f vector) {
		return new Vector3(vector.getX(), vector.getY(), vector.getZ());
	}

	@SideOnly(Side.CLIENT)
	public static Vector3 convertVector(Vec3 vector) {
		return new Vector3((float)vector.xCoord, (float)vector.yCoord, (float)vector.zCoord);
	}

	@SideOnly(Side.CLIENT)
	public static Quaternion quatLerp(Quaternion start, Quaternion end, float fraction) {
		Quaternion quat = new Quaternion();
		quat.w = start.w + (end.w - start.w) * fraction;
		quat.x = start.x + (end.x - start.x) * fraction;
		quat.y = start.y + (end.y - start.y) * fraction;
		quat.z = start.z + (end.z - start.z) * fraction;
		return quat;
	}

	@SideOnly(Side.CLIENT)
	public static Matrix4f matrix3to4(Matrix3f matrix) {
		Matrix4f mat = new Matrix4f();
		mat.m00 = matrix.m00;
		mat.m01 = matrix.m01;
		mat.m02 = matrix.m02;
		mat.m10 = matrix.m10;
		mat.m11 = matrix.m11;
		mat.m12 = matrix.m12;
		mat.m20 = matrix.m20;
		mat.m21 = matrix.m21;
		mat.m22 = matrix.m22;
		return mat;
	}
}
