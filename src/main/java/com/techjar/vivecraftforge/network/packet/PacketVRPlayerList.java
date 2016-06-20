package com.techjar.vivecraftforge.network.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.entity.EntityVRObject;
import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.proxy.ProxyClient;
import com.techjar.vivecraftforge.util.VRPlayerData;
import com.techjar.vivecraftforge.util.VivecraftReflector;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;

public class PacketVRPlayerList implements IPacket {
	public Map<Integer, VRPlayerData> entityIds;

	public PacketVRPlayerList() {
	}

	public PacketVRPlayerList(Map<EntityPlayer, VRPlayerData> entityMap) {
		entityIds = new HashMap<Integer, VRPlayerData>(entityMap.size());
		for (Map.Entry<EntityPlayer, VRPlayerData> entry : entityMap.entrySet()) {
			entityIds.put(entry.getKey().getEntityId(), entry.getValue().copy());
		}
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		ByteBufUtils.writeVarInt(buffer, entityIds.size(), 5);
		for (Map.Entry<Integer, VRPlayerData> entry : entityIds.entrySet()) {
			ByteBufUtils.writeVarInt(buffer, entry.getKey(), 5);
			buffer.writeBoolean(entry.getValue().handsSwapped);
			buffer.writeByte(entry.getValue().entityIds.size());
			for (int id : entry.getValue().entityIds) {
				ByteBufUtils.writeVarInt(buffer, id, 5);
			}
		}
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		int size = ByteBufUtils.readVarInt(buffer, 5);
		entityIds = new HashMap<Integer, VRPlayerData>(size);
		for (int i = 0; i < size; i++) {
			VRPlayerData data = new VRPlayerData();
			entityIds.put(ByteBufUtils.readVarInt(buffer, 5), data);
			data.handsSwapped = buffer.readBoolean();
			int size2 = buffer.readUnsignedByte();
			for (int j = 0; j < size2; j++) {
				data.entityIds.add(ByteBufUtils.readVarInt(buffer, 5));
			}
		}
	}

	@Override
	public void handleClient(EntityPlayer player) {
		ProxyClient.vrPlayerIds = entityIds;
	}

	@Override
	public void handleServer(EntityPlayer player) {
	}
}
