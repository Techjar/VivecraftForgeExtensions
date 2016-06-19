package com.techjar.vivecraftforge.handler;

import net.minecraft.util.Vec3;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.network.packet.PacketVRData;
import com.techjar.vivecraftforge.util.Quaternion;
import com.techjar.vivecraftforge.util.VivecraftReflector;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class HandlerClientTick {
	@SubscribeEvent
	public void onClientTick(ClientTickEvent event) { // TODO
		if (event.phase == Phase.END && VivecraftReflector.isInstalled()) {
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
}
