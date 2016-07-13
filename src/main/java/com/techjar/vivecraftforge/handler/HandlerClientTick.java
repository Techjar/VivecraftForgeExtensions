package com.techjar.vivecraftforge.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.network.packet.PacketVRSettings;
import com.techjar.vivecraftforge.network.packet.PacketVRData;
import com.techjar.vivecraftforge.proxy.ProxyClient;
import com.techjar.vivecraftforge.util.Quaternion;
import com.techjar.vivecraftforge.util.VivecraftReflector;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;

public class HandlerClientTick {
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) { // TODO
		if (event.phase == Phase.END && ProxyClient.isVFEServer && VivecraftReflector.isInstalled()) {
			if (VivecraftReflector.isNewAPI()) {
				boolean reverseHands = VivecraftReflector.getReverseHands();
				float worldScale = VivecraftReflector.getWorldScale();
				boolean seated = VivecraftReflector.getSeated();
				if (reverseHands != ProxyClient.reverseHandsLast || worldScale != ProxyClient.worldScaleLast || seated != ProxyClient.seatedLast) {
					VivecraftForge.packetPipeline.sendToServer(new PacketVRSettings(VivecraftReflector.getReverseHands(), worldScale, VivecraftReflector.getSeated()));
					ProxyClient.reverseHandsLast = reverseHands;
					ProxyClient.worldScaleLast = worldScale;
					ProxyClient.seatedLast = seated;
				}
			} else {
				boolean reverseHands = VivecraftReflector.getReverseHands();
				if (reverseHands != ProxyClient.reverseHandsLast) {
					VivecraftForge.packetPipeline.sendToServer(new PacketVRSettings(VivecraftReflector.getReverseHands(), 1, false));
					ProxyClient.reverseHandsLast = reverseHands;
				}
			}
			Vec3 headPosition = VivecraftReflector.getHeadPosition();
			Quaternion headRotation = new Quaternion(VivecraftReflector.getHeadRotation());
			VivecraftForge.packetPipeline.sendToServer(new PacketVRData(0, headPosition, headRotation));
			for (int i = 0; i < 2; i++) {
				Vec3 controllerPosition = VivecraftReflector.getControllerPositon(i);
				Quaternion controllerRotation = new Quaternion(VivecraftReflector.getControllerRotation(i));
				VivecraftForge.packetPipeline.sendToServer(new PacketVRData(1 + i, controllerPosition, controllerRotation));
			}
		}
	}
	
	@SubscribeEvent
	public void onClientDisconnect(ClientDisconnectionFromServerEvent event) {
		ProxyClient.isVFEServer = false;
		ProxyClient.reverseHandsLast = false;
		ProxyClient.worldScaleLast = 0;
		ProxyClient.seatedLast = false;
		ProxyClient.vrPlayerIds.clear();
	}
}
