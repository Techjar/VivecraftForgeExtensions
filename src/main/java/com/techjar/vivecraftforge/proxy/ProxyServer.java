package com.techjar.vivecraftforge.proxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.entity.EntityVRObject;
import com.techjar.vivecraftforge.handler.HandlerRenderEvent;
import com.techjar.vivecraftforge.network.ViveMessage;
import com.techjar.vivecraftforge.util.VRPlayerData;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

public class ProxyServer extends ProxyCommon {
	public static Map<EntityPlayer, VRPlayerData> vrPlayers = new HashMap<EntityPlayer, VRPlayerData>();
	
	@Override
	public void registerNetworkChannels() {
		super.registerNetworkChannels();
		VivecraftForge.networkVersion = NetworkRegistry.INSTANCE.newSimpleChannel("MC|Vive|Version");
		//VivecraftForge.networkFreeMove = NetworkRegistry.INSTANCE.newSimpleChannel("MC|Vive|FreeMove"); // currently not used
		VivecraftForge.networkLegacy = NetworkRegistry.INSTANCE.newSimpleChannel("MC|Vive");
		VivecraftForge.networkOK = NetworkRegistry.INSTANCE.newSimpleChannel("MC|ViveOK");
		VivecraftForge.networkVersion.registerMessage(ViveMessage.Handle.class, ViveMessage.class, 86, Side.SERVER);
		VivecraftForge.networkLegacy.registerMessage(ViveMessage.Handle.class, ViveMessage.class, 112, Side.SERVER);
	}

	public static boolean isVRPlayer(EntityPlayer entity) {
		return ProxyServer.vrPlayers.containsKey(entity);
	}
	
	public static boolean getVRPlayerSeated(EntityPlayer entity) {
		VRPlayerData data = ProxyServer.vrPlayers.get(entity);
		if (data != null) {
			return data.seated;
		}
		return false;
	}
}
