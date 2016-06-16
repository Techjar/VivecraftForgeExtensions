package com.techjar.vivecraftforge.util;

import org.apache.logging.log4j.Level;

import cpw.mods.fml.relauncher.FMLRelaunchLog;

public class VivecraftForgeLog {
	public static void info(String message, Object... data) {
		FMLRelaunchLog.log("Vivecraft Forge Extensions", Level.INFO, message, data);
	}

	public static void warning(String message, Object... data) {
		FMLRelaunchLog.log("Vivecraft Forge Extensions", Level.WARN, message, data);
	}

	public static void severe(String message, Object... data) {
		FMLRelaunchLog.log("Vivecraft Forge Extensions", Level.ERROR, message, data);
	}

	public static void debug(String message, Object... data) {
		FMLRelaunchLog.log("Vivecraft Forge Extensions", Level.DEBUG, message, data);
	}
}