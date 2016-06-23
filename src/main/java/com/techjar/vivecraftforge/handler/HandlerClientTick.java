package com.techjar.vivecraftforge.handler;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Vec3;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.network.packet.PacketPlayerWorldScale;
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
				float worldScale = VivecraftReflector.getWorldScale();
				if (worldScale != ProxyClient.worldScaleLast) {
					VivecraftForge.packetPipeline.sendToServer(new PacketPlayerWorldScale(worldScale));
					ProxyClient.worldScaleLast = worldScale;
				}
			}
			Vec3 headPosition = VivecraftReflector.getHeadPosition();
			Quaternion headRotation = new Quaternion(VivecraftReflector.getHeadRotation());
			VivecraftForge.packetPipeline.sendToServer(new PacketVRData(0, headPosition, headRotation, VivecraftReflector.getReverseHands()));
			for (int i = 0; i < 2; i++) {
				Vec3 controllerPosition = VivecraftReflector.getControllerPositon(i);
				Quaternion controllerRotation = new Quaternion(VivecraftReflector.getControllerRotation(i));
				VivecraftForge.packetPipeline.sendToServer(new PacketVRData(1 + i, controllerPosition, controllerRotation, VivecraftReflector.getReverseHands()));
			}
		}
	}
	
	@SubscribeEvent
	public void onClientDisconnect(ClientDisconnectionFromServerEvent event) {
		ProxyClient.isVFEServer = false;
		ProxyClient.worldScaleLast = 0;
		ProxyClient.vrPlayerIds.clear();
		System.out.println("lolo");
	}
}
