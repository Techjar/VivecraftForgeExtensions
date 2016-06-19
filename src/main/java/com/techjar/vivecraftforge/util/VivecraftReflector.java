package com.techjar.vivecraftforge.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Matrix4f;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.Vec3;

/**
 * This class is responsible for hooking into Vivecraft code reflectively so we can get the position info
 * we need without creating a hard dependency on Vivecraft, enabling normal clients to use this mod as well.
 */
@SideOnly(Side.CLIENT)
public class VivecraftReflector {
	private VivecraftReflector() {
	}

	// Stupid JRift Matrix4f
	private static Field field_M;
	// New API
	private static Method method_getHMDPos_World;
	private static Method method_getHMDMatrix_World;
	private static Method method_getControllerMainPos_World;
	private static Method method_getControllerOffhandPos_World;
	private static Method method_getControllerMatrix_World;
	private static Field field_vrPlayer;
	// Old API
	private static Method method_getCameraLocation;
	private static Method method_getAimSource;
	private static Method method_getAimRotation;
	private static Field field_lookaimController;
	private static Field field_hmdPose;
	// Other stuff
	private static Field field_vrSettings;
	private static Field field_vrReverseHands;
	private static boolean installed;
	private static boolean newAPI;
	static {
		try {
			Class.forName("com.mtbs3d.minecrift.provider.MCOpenVR", false, null);
			installed = true;
		} catch (ClassNotFoundException ex) {
			installed = false;
		}
		try {
			Class.forName("com.mtbs3d.minecrift.api.IRoomscaleProvider", false, null);
			newAPI = true;
		} catch (ClassNotFoundException ex) {
			newAPI = false;
		}
	}

	public static boolean isInstalled() {
		return installed;
	}

	public static boolean isNewAPI() {
		return newAPI;
	}

	@SneakyThrows(Exception.class)
	public static boolean getReverseHands() {
		if (field_vrSettings == null) {
			field_vrSettings = Minecraft.class.getDeclaredField("vrSettings");
			field_vrReverseHands = Class.forName("com.mtbs3d.minecrift.settings.VRSettings").getDeclaredField("vrReverseHands");
		}
		return field_vrReverseHands.getBoolean((field_vrSettings.get(Minecraft.getMinecraft())));
	}

	@SneakyThrows(Exception.class)
	public static Vec3 getHeadPosition() {
		if (newAPI) {
			if (method_getHMDPos_World == null) {
				method_getHMDPos_World = Class.forName("com.mtbs3d.minecrift.api.IRoomscaleProvider").getMethod("getHMDPos_World");
			}
			return (Vec3)method_getHMDPos_World.invoke(getVRPlayer());
		} else {
			if (method_getCameraLocation == null) {
				method_getCameraLocation = EntityRenderer.class.getDeclaredMethod("getCameraLocation");
			}
			return (Vec3)method_getCameraLocation.invoke(Minecraft.getMinecraft().entityRenderer);
		}
	}

	@SneakyThrows(Exception.class)
	public static Matrix4f getHeadRotation() {
		if (newAPI) {
			if (method_getHMDMatrix_World == null) {
				method_getHMDMatrix_World = Class.forName("com.mtbs3d.minecrift.api.IRoomscaleProvider").getMethod("getHMDMatrix_World");
			}
			FloatBuffer buffer = (FloatBuffer)method_getHMDMatrix_World.invoke(getVRPlayer());
			buffer.rewind();
			Matrix4f matrix = new Matrix4f();
			matrix.load(buffer);
			return matrix;
		} else {
			if (field_hmdPose == null) {
				field_hmdPose = Class.forName("com.mtbs3d.minecrift.provider.MCOpenVR").getDeclaredField("hmdPose");
			}
			return convertMatrix(field_hmdPose.get(null));
		}
	}

	/**
	 * 0 for main, 1 for off-hand
	 */
	@SneakyThrows(Exception.class)
	public static Vec3 getControllerPositon(int controller) {
		if (newAPI) {
			if (method_getControllerMainPos_World == null || method_getControllerOffhandPos_World == null) {
				method_getControllerMainPos_World = Class.forName("com.mtbs3d.minecrift.api.IRoomscaleProvider").getMethod("getControllerMainPos_World");
				method_getControllerOffhandPos_World = Class.forName("com.mtbs3d.minecrift.api.IRoomscaleProvider").getMethod("getControllerOffhandPos_World");
			}
			if (controller == 0) {
				return (Vec3)method_getControllerMainPos_World.invoke(getVRPlayer());
			} else if (controller == 1) {
				return (Vec3)method_getControllerOffhandPos_World.invoke(getVRPlayer());
			} else {
				throw new IllegalArgumentException("Invalid controller ID: " + controller);
			}
		} else {
			if (method_getAimSource == null) {
				method_getAimSource = Class.forName("com.mtbs3d.minecrift.api.IBodyAimController").getDeclaredMethod("getAimSource", Integer.TYPE);
			}
			return (Vec3)method_getAimSource.invoke(getAimController(), controller);
		}
	}

	/**
	 * 0 for main, 1 for off-hand
	 */
	@SneakyThrows(Exception.class)
	public static Matrix4f getControllerRotation(int controller) {
		if (newAPI) {
			if (method_getControllerMatrix_World == null) {
				method_getControllerMatrix_World = Class.forName("com.mtbs3d.minecrift.api.IRoomscaleProvider").getMethod("getControllerMatrix_World", Integer.TYPE);
			}
			FloatBuffer buffer = (FloatBuffer)method_getControllerMatrix_World.invoke(getVRPlayer(), controller);
			buffer.rewind();
			Matrix4f matrix = new Matrix4f();
			matrix.load(buffer);
			return matrix;
		} else {
			if (method_getAimRotation == null) {
				method_getAimRotation = Class.forName("com.mtbs3d.minecrift.api.IBodyAimController").getDeclaredMethod("getAimRotation", Integer.TYPE);
			}
			return convertMatrix(method_getAimRotation.invoke(getAimController(), controller));
		}
	}

	@SneakyThrows(Exception.class)
	private static Object getAimController() {
		if (field_lookaimController == null) {
			field_lookaimController = Minecraft.class.getDeclaredField("lookaimController");
		}
		return field_lookaimController.get(Minecraft.getMinecraft());
	}

	@SneakyThrows(Exception.class)
	private static Object getVRPlayer() {
		if (field_vrPlayer == null) {
			field_vrPlayer = Minecraft.class.getDeclaredField("vrPlayer");
		}
		return field_vrPlayer.get(Minecraft.getMinecraft());
	}

	@SneakyThrows(Exception.class)
	private static Matrix4f convertMatrix(Object object) {
		if (field_M == null) {
			field_M = Class.forName("de.fruitfly.ovr.structs.Matrix4f").getDeclaredField("M");
		}
		float[][] array = (float[][])field_M.get(object);
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = array[0][0];
		matrix.m01 = array[0][1];
		matrix.m02 = array[0][2];
		matrix.m03 = array[0][3];
		matrix.m10 = array[1][0];
		matrix.m11 = array[1][1];
		matrix.m12 = array[1][2];
		matrix.m13 = array[1][3];
		matrix.m20 = array[2][0];
		matrix.m21 = array[2][1];
		matrix.m22 = array[2][2];
		matrix.m23 = array[2][3];
		matrix.m30 = array[3][0];
		matrix.m31 = array[3][1];
		matrix.m32 = array[3][2];
		matrix.m33 = array[3][3];
		return matrix;
	}
}
