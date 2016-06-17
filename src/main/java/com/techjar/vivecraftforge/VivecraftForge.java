package com.techjar.vivecraftforge;

import com.techjar.vivecraftforge.network.ViveMessage;
import com.techjar.vivecraftforge.network.VivecraftForgeChannelHandler;
import com.techjar.vivecraftforge.proxy.ProxyCommon;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "VivecraftForge", name = "Vivecraft Forge Extensions", version = "@VERSION@", dependencies = "required-after:Forge@[10.13.0.1207,)", acceptableRemoteVersions = "@RAW_VERSION@.*")
public class VivecraftForge {
	@Instance("VivecraftForge")
	public static VivecraftForge instance;
	
	@SidedProxy(clientSide = "com.techjar.vivecraftforge.proxy.ProxyClient", serverSide = "com.techjar.vivecraftforge.proxy.ProxyServer")
	public static ProxyCommon proxy;

	public static SimpleNetworkWrapper networkVersion;
	//public static SimpleNetworkWrapper networkFreeMove; // currently not used
	public static SimpleNetworkWrapper networkLegacy;
	public static SimpleNetworkWrapper networkOK;
	
	public static VivecraftForgeChannelHandler packetPipeline;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// Stub Method
	}

	@EventHandler
	public void load(FMLInitializationEvent event) {
		networkVersion = NetworkRegistry.INSTANCE.newSimpleChannel("MC|Vive|Version");
		//networkFreeMove = NetworkRegistry.INSTANCE.newSimpleChannel("MC|Vive|FreeMove"); // currently not used
		networkLegacy = NetworkRegistry.INSTANCE.newSimpleChannel("MC|Vive");
		networkOK = NetworkRegistry.INSTANCE.newSimpleChannel("MC|ViveOK");
		networkVersion.registerMessage(ViveMessage.Handle.class, ViveMessage.class, 86, Side.SERVER);
		networkLegacy.registerMessage(ViveMessage.Handle.class, ViveMessage.class, 112, Side.SERVER);

		packetPipeline = VivecraftForgeChannelHandler.init();
		
		proxy.registerEventHandlers();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
}
