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

public class PacketPlayerWorldScale implements IPacket {
	public float scale;

	public PacketPlayerWorldScale() {
	}

	public PacketPlayerWorldScale(float scale) {
		this.scale = scale;
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeFloat(this.scale);
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		this.scale = buffer.readFloat();
	}

	@Override
	public void handleClient(EntityPlayer player) {
	}

	@Override
	public void handleServer(EntityPlayer player) {
		if (ProxyServer.vrPlayers.containsKey(player)) {
			VRPlayerData data = ProxyServer.vrPlayers.get(player);
			data.worldScale = scale;
			VivecraftForge.packetPipeline.sendToAll(new PacketVRPlayerList(ProxyServer.vrPlayers));
		}
	}
}
