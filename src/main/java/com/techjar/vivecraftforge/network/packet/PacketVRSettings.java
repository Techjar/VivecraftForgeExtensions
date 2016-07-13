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

public class PacketVRSettings implements IPacket {
	public boolean reverseHands;
	public float scale;
	public boolean seated;

	public PacketVRSettings() {
	}

	public PacketVRSettings(boolean reverseHands, float scale, boolean seated) {
		this.reverseHands = reverseHands;
		this.scale = scale;
		this.seated = seated;
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeBoolean(this.reverseHands);
		buffer.writeFloat(this.scale);
		buffer.writeBoolean(this.seated);
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		this.reverseHands = buffer.readBoolean();
		this.scale = buffer.readFloat();
		this.seated = buffer.readBoolean();
	}

	@Override
	public void handleClient(EntityPlayer player) {
	}

	@Override
	public void handleServer(EntityPlayer player) {
		if (ProxyServer.vrPlayers.containsKey(player)) {
			VRPlayerData data = ProxyServer.vrPlayers.get(player);
			data.reverseHands = reverseHands;
			data.worldScale = scale;
			data.seated = seated;
			VivecraftForge.packetPipeline.sendToAll(new PacketVRPlayerList(ProxyServer.vrPlayers));
		}
	}
}
