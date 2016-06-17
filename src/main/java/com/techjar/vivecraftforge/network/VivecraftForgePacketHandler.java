package com.techjar.vivecraftforge.network;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

@Sharable
public class VivecraftForgePacketHandler extends SimpleChannelInboundHandler<IPacket> {
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, IPacket msg) throws Exception {
		INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
		EntityPlayer player = VivecraftForge.proxy.getPlayerFromNetHandler(netHandler);

		switch (FMLCommonHandler.instance().getEffectiveSide()) {
			case CLIENT:
				msg.handleClient(player);
				break;
			case SERVER:
				msg.handleServer(player);
				break;
			default:
				VivecraftForgeLog.severe("Impossible scenario encountered! Effective side is neither server nor client!");
				break;
		}
	}
}
