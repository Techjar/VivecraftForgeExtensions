package com.techjar.vivecraftforge.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.util.Vec3;

/**
 * This class is responsible for hooking into Vivecraft code reflectively so we can get the position info
 * we need without creating a hard dependency on Vivecraft, enabling normal clients to use this mod as well.
 */
public class VivecraftReflector {
	private VivecraftReflector() {
	}
	
	private static Method method_getCameraLocation;
	private static Method method_getAimSource;
	private static Method method_getAimVector;
	private static Field field_lookaimController;
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
	public static Vec3 getControllerAim(int controller) {
		if (method_getAimVector == null) {
			method_getAimVector = Class.forName("com.mtbs3d.minecrift.api.IBodyAimController").getDeclaredMethod("getAimVector", Integer.TYPE);
		}
		return (Vec3)method_getAimVector.invoke(getAimController(), controller);
	}
	
	@SneakyThrows(Exception.class)
	private static Object getAimController() {
		if (field_lookaimController == null) {
			field_lookaimController = Minecraft.class.getDeclaredField("lookaimController");
		}
		return field_lookaimController.get(Minecraft.getMinecraft());
	}
}
