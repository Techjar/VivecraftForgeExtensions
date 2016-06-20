package com.techjar.vivecraftforge.handler;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.entity.EntityVRHead;
import com.techjar.vivecraftforge.entity.EntityVRMainArm;
import com.techjar.vivecraftforge.entity.EntityVROffHandArm;
import com.techjar.vivecraftforge.network.packet.PacketInitialize;
import com.techjar.vivecraftforge.network.packet.PacketVRPlayerList;
import com.techjar.vivecraftforge.proxy.ProxyServer;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HandlerEntityEvent {
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!event.world.isRemote && event.entity instanceof EntityPlayerMP) {
			VivecraftForge.packetPipeline.sendToPlayer(new PacketVRPlayerList(ProxyServer.vrPlayers), (EntityPlayerMP)event.entity);
			VivecraftForge.packetPipeline.sendToPlayer(new PacketInitialize(), (EntityPlayerMP)event.entity);
		}
	}
}
