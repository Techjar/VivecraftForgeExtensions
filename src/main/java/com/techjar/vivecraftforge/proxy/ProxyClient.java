package com.techjar.vivecraftforge.proxy;

import java.util.HashMap;
import java.util.Map;

import com.techjar.vivecraftforge.client.render.entity.*;
import com.techjar.vivecraftforge.entity.*;
import com.techjar.vivecraftforge.handler.HandlerClientTick;
import com.techjar.vivecraftforge.handler.HandlerRenderEvent;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VRPlayerData;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ProxyClient extends ProxyCommon {
	public static boolean isVFEServer;
	public static boolean reverseHandsLast;
	public static float worldScaleLast;
	public static boolean seatedLast;
	public static Map<Integer, VRPlayerData> vrPlayerIds = new HashMap<Integer, VRPlayerData>();

	@Override
	public void registerRenderers() {
		this.registerEntityRenderers();
	}

	@Override
	public void registerEventHandlers() {
		super.registerEventHandlers();
		MinecraftForge.EVENT_BUS.register(new HandlerRenderEvent());
		FMLCommonHandler.instance().bus().register(new HandlerClientTick());
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

	public static boolean isVRPlayer(EntityPlayer entity) {
		return entity != Minecraft.getMinecraft().thePlayer && ProxyClient.vrPlayerIds.containsKey(entity.getEntityId());
	}

	public static float getVRPlayerHeadHeight(EntityPlayer entity) {
		VRPlayerData data = ProxyClient.vrPlayerIds.get(entity.getEntityId());
		if (data != null && data.entityIds.size() > 0) {
			EntityVRObject entityVR = (EntityVRObject)entity.worldObj.getEntityByID(data.entityIds.get(0));
			if (entityVR != null) {
				return (float)entityVR.position.yCoord;
			}
		}
		return (float)entity.posY + 1.62F;
	}

	public static float getVRPlayerScale(EntityPlayer entity) {
		float height = getVRPlayerHeadHeight((EntityPlayer)entity) - 0.125F;
		return Math.max((height - (float)entity.posY) / 1.62F, 0.1F);
	}

	public static float getVRPlayerWorldScale(EntityPlayer entity) {
		VRPlayerData data = ProxyClient.vrPlayerIds.get(entity.getEntityId());
		if (data != null) {
			return data.worldScale;
		}
		return 1;
	}

	public static boolean getVRPlayerSeated(EntityPlayer entity) {
		VRPlayerData data = ProxyClient.vrPlayerIds.get(entity.getEntityId());
		if (data != null) {
			return data.seated;
		}
		return false;
	}
}
