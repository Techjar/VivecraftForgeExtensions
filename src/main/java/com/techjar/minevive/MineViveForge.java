package com.techjar.minevive;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "MineViveForge", name = "MineViveForge", version = "1.0", acceptableRemoteVersions = "*")
public class MineViveForge {
	@Instance("MineViveForge")
	public static MineViveForge instance;

	public static SimpleNetworkWrapper network;
	public static SimpleNetworkWrapper network2;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Stub Method
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		network = NetworkRegistry.INSTANCE.newSimpleChannel("MC|Vive|Version");
		network2 = NetworkRegistry.INSTANCE.newSimpleChannel("MC|Vive|OK");
		network.registerMessage(ViveMessage.Handle.class, ViveMessage.class, 112, Side.SERVER);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
}
