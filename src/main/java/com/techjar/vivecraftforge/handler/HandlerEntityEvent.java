package com.techjar.vivecraftforge.handler;

import com.techjar.vivecraftforge.entity.EntityVRHead;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HandlerEntityEvent {
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!event.world.isRemote && event.entity instanceof EntityPlayer) {
			Entity entityVR = new EntityVRHead(event.world);
			//System.out.println(event.entity.posX + " " + event.entity.posY + " " + event.entity.posZ);
			entityVR.setPosition(event.entity.posX, event.entity.posY + 5, event.entity.posZ);
			event.world.spawnEntityInWorld(entityVR);
		}
	}
}
