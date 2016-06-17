package com.techjar.vivecraftforge.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;

public interface IPacket {
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer);

	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer);

	public void handleClient(EntityPlayer player);

	public void handleServer(EntityPlayer player);
}
