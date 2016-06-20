package com.techjar.vivecraftforge.network.packet;

import java.util.ArrayList;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.entity.EntityVRObject;
import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.proxy.ProxyClient;
import com.techjar.vivecraftforge.proxy.ProxyServer;
import com.techjar.vivecraftforge.util.VRPlayerData;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;
import com.techjar.vivecraftforge.util.VivecraftReflector;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketInitialize implements IPacket {
	public boolean installed;

	public PacketInitialize() {
	}

	public PacketInitialize(boolean installed) {
		this.installed = installed;
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeBoolean(this.installed);
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		this.installed = buffer.readBoolean();
	}

	@Override
	public void handleClient(EntityPlayer player) {
		ProxyClient.isVFEServer = true;
		VivecraftForgeLog.debug("Received init packet");
		VivecraftForge.packetPipeline.sendToServer(new PacketInitialize(VivecraftReflector.isInstalled()));
	}

	@Override
	public void handleServer(EntityPlayer player) {
		//EntityPlayerMP pl = (EntityPlayerMP)player;
		if (installed) {
			VivecraftForgeLog.info("VR player joined!");
			if (!ProxyServer.vrPlayers.containsKey(player))
				ProxyServer.vrPlayers.put(player, new VRPlayerData());
			VivecraftForge.packetPipeline.sendToAll(new PacketVRPlayerList(ProxyServer.vrPlayers));
		}
	}
}
