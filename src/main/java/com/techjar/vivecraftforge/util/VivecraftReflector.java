package com.techjar.vivecraftforge.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

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
	
	private static Method method_getCameraLocation;
	private static Method method_getAimSource;
	private static Method method_getAimRotation;
	private static Field field_lookaimController;
	private static Field field_hmdPose;
	private static boolean installed;
	static {
		try {
			Class.forName("com.mtbs3d.minecrift.provider.MCOpenVR", false, null);
			installed = true;
		} catch (ClassNotFoundException ex) {
			installed = false;
		}
	}
	
	public static boolean isInstalled() {
		return installed;
	}
	
	@SneakyThrows(Exception.class)
	public static Vec3 getHeadPosition() {
		if (method_getCameraLocation == null) {
			method_getCameraLocation = EntityRenderer.class.getDeclaredMethod("getCameraLocation");
		}
		return (Vec3)method_getCameraLocation.invoke(Minecraft.getMinecraft().entityRenderer);
	}
	
	@SneakyThrows(Exception.class)
	public static Matrix4f getHeadRotation() {
		if (field_hmdPose == null) {
			field_hmdPose = Class.forName("com.mtbs3d.minecrift.provider.MCOpenVR").getDeclaredField("hmdPose");
		}
		return (Matrix4f)field_hmdPose.get(null);
	}
	
	/**
	 * 0 for main, 1 for off-hand
	 */
	@SneakyThrows(Exception.class)
	public static Vec3 getControllerPositon(int controller) {
		if (method_getAimSource == null) {
			method_getAimSource = Class.forName("com.mtbs3d.minecrift.api.IBodyAimController").getDeclaredMethod("getAimSource", Integer.TYPE);
		}
		return (Vec3)method_getAimSource.invoke(getAimController(), controller);
	}
	
	/**
	 * 0 for main, 1 for off-hand
	 */
	@SneakyThrows(Exception.class)
	public static Matrix4f getControllerRotation(int controller) {
		if (method_getAimRotation == null) {
			method_getAimRotation = Class.forName("com.mtbs3d.minecrift.api.IBodyAimController").getDeclaredMethod("getAimRotation", Integer.TYPE);
		}
		return (Matrix4f)method_getAimRotation.invoke(getAimController(), controller);
	}
	
	@SneakyThrows(Exception.class)
	private static Object getAimController() {
		if (field_lookaimController == null) {
			field_lookaimController = Minecraft.class.getDeclaredField("lookaimController");
		}
		return field_lookaimController.get(Minecraft.getMinecraft());
	}
}
