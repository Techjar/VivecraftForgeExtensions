package com.techjar.vivecraftforge.network.packet;

import com.techjar.vivecraftforge.network.IPacket;

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
		EntityPlayerSP pl = (EntityPlayerSP)player;
		Minecraft client = FMLClientHandler.instance().getClient();
	}

	@Override
	public void handleServer(EntityPlayer player) {
		EntityPlayerMP pl = (EntityPlayerMP)player;
	}
}
