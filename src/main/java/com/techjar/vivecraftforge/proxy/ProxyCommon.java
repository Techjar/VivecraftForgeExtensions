package com.techjar.vivecraftforge.proxy;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.entity.EntityVRHead;
import com.techjar.vivecraftforge.entity.EntityVRObject;
import com.techjar.vivecraftforge.handler.HandlerEntityEvent;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ProxyCommon {
	public void registerRenderers() {
		// Nothing here as the server doesn't render graphics!
	}
	public void registerEntities() {
		EntityRegistry.registerModEntity(EntityVRHead.class, "VRHead", 0, VivecraftForge.instance, 9999, 1, false);
	}
	
	public void registerEventHandlers() {
		MinecraftForge.EVENT_BUS.register(new HandlerEntityEvent());
	}

	public EntityPlayer getPlayerFromNetHandler(INetHandler netHandler) {
		if (netHandler instanceof NetHandlerPlayServer) {
			return ((NetHandlerPlayServer)netHandler).playerEntity;
		}
		return null;
	}
}
