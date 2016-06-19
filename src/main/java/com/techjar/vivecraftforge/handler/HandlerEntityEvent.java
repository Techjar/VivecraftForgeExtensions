package com.techjar.vivecraftforge.handler;

import com.techjar.vivecraftforge.entity.EntityVRHead;
import com.techjar.vivecraftforge.entity.EntityVRMainArm;
import com.techjar.vivecraftforge.entity.EntityVROffHandArm;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class HandlerEntityEvent {
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		if (!event.world.isRemote && event.entity instanceof EntityPlayer) { // TODO
			Entity entityVR = new EntityVROffHandArm(event.world);
			entityVR.setPosition(event.entity.posX, event.entity.posY + 5, event.entity.posZ);
			event.world.spawnEntityInWorld(entityVR);
		}
	}
}
