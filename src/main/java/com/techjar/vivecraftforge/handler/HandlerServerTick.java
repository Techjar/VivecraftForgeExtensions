package com.techjar.vivecraftforge.handler;

import java.util.Iterator;
import java.util.Map;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.entity.EntityVRHead;
import com.techjar.vivecraftforge.entity.EntityVRMainArm;
import com.techjar.vivecraftforge.entity.EntityVRObject;
import com.techjar.vivecraftforge.entity.EntityVROffHandArm;
import com.techjar.vivecraftforge.network.packet.PacketVRPlayerList;
import com.techjar.vivecraftforge.proxy.ProxyServer;
import com.techjar.vivecraftforge.util.VRPlayerData;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;

public class HandlerServerTick {
	@SubscribeEvent
	public void onServerTick(ServerTickEvent event) {
		if (event.phase == Phase.START) {
			for (Iterator<Map.Entry<EntityPlayer, VRPlayerData>> it = ProxyServer.vrPlayers.entrySet().iterator(); it.hasNext(); ) {
				Map.Entry<EntityPlayer, VRPlayerData> entry = it.next();
				EntityPlayer player = entry.getKey();
				if (player.isDead) {
					it.remove();
					continue;
				}
				VRPlayerData data = entry.getValue();
				if (data.entities.size() != 3) {
					createEntities(player, data);
				} else {
					for (EntityVRObject entity : data.entities) {
						//System.out.println(entity.getClass().getSimpleName() + " " + entity.posX + " " + entity.posY + " " + entity.posZ);
						if (!entity.isSpawned()) {
							if (entity.worldObj.spawnEntityInWorld(entity)) entity.setSpawned();
						}
						if (entity.isDead || entity.worldObj != player.worldObj) {
							createEntities(player, data);
							break;
						}
					}
				}
			}
		}
	}
	
	private void createEntities(EntityPlayer player, VRPlayerData data) {
		VivecraftForgeLog.debug("Creating new entities for %s", player);
		for (Entity entity : data.entities) {
			entity.setDead();
		}
		data.entities.clear();
		data.entityIds.clear();
		data.entities.add(new EntityVRHead(player.worldObj, player.getEntityId()));
		data.entities.add(new EntityVRMainArm(player.worldObj, player.getEntityId()));
		data.entities.add(new EntityVROffHandArm(player.worldObj, player.getEntityId()));
		data.entityIds.add(data.entities.get(0).getEntityId());
		data.entityIds.add(data.entities.get(1).getEntityId());
		data.entityIds.add(data.entities.get(2).getEntityId());
		VivecraftForge.packetPipeline.sendToAll(new PacketVRPlayerList(ProxyServer.vrPlayers));
	}
}
