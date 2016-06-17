package com.techjar.vivecraftforge.proxy;

import java.lang.reflect.*;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFire;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class ProxyCommon {
	public void registerRenderers() {
		// Nothing here as the server doesn't render graphics!
	}
	
	public void registerEventHandlers() {
	}

	public abstract EntityPlayer getPlayerFromNetHandler(INetHandler netHandler);
}
