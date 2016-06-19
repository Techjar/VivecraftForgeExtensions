package com.techjar.vivecraftforge.proxy;

import com.techjar.vivecraftforge.client.render.entity.*;
import com.techjar.vivecraftforge.entity.*;
import com.techjar.vivecraftforge.handler.HandlerRenderEvent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ProxyClient extends ProxyCommon {
	@Override
	public void registerRenderers() {
		this.registerEntityRenderers();
	}

	@Override
	public void registerEventHandlers() {
		super.registerEventHandlers();
		MinecraftForge.EVENT_BUS.register(new HandlerRenderEvent());
	}

	@Override
	public EntityPlayer getPlayerFromNetHandler(INetHandler netHandler) {
		if (netHandler instanceof NetHandlerPlayServer) {
			return ((NetHandlerPlayServer)netHandler).playerEntity;
		}
		return FMLClientHandler.instance().getClientPlayerEntity();
	}

	private void registerEntityRenderers() {
		RenderingRegistry.registerEntityRenderingHandler(EntityVRHead.class, new RenderEntityVRHead());
		RenderingRegistry.registerEntityRenderingHandler(EntityVRMainArm.class, new RenderEntityVRMainArm());
		RenderingRegistry.registerEntityRenderingHandler(EntityVROffHandArm.class, new RenderEntityVROffHandArm());
	}
}
