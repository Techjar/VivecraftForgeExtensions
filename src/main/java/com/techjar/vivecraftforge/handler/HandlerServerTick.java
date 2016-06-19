package com.techjar.vivecraftforge.handler;

import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import com.techjar.vivecraftforge.entity.EntityVRHead;
import com.techjar.vivecraftforge.entity.EntityVRMainArm;
import com.techjar.vivecraftforge.entity.EntityVROffHandArm;
import com.techjar.vivecraftforge.proxy.ProxyServer;
import com.techjar.vivecraftforge.util.VRPlayerData;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class HandlerServerTick {
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		if (event.phase == Phase.START) {
			for (Map.Entry<EntityPlayer, VRPlayerData> entry : ProxyServer.vrPlayers.entrySet()) {
				EntityPlayer player = entry.getKey();
				VRPlayerData data = entry.getValue();
				if (data.entities.size() != 3) {
					createEntities(player, data);
				} else {
					for (Entity entity : data.entities) {
						if (entity.isDead) {
							createEntities(player, data);
							break;
						}
					}
				}
			}
		}
	}
	
	private void createEntities(EntityPlayer player, VRPlayerData data) {
		for (Entity entity : data.entities) {
			entity.setDead();
		}
		data.entities.clear();
		data.entities.add(new EntityVRHead(player.worldObj));
		data.entities.add(new EntityVRMainArm(player.worldObj));
		data.entities.add(new EntityVROffHandArm(player.worldObj));
		for (Entity entity : data.entities) {
			player.worldObj.spawnEntityInWorld(entity);
		}
	}
}
