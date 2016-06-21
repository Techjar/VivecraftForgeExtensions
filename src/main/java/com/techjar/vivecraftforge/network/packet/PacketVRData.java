package com.techjar.vivecraftforge.network.packet;

import java.util.List;

import com.techjar.vivecraftforge.VivecraftForge;
import com.techjar.vivecraftforge.entity.EntityVRArm;
import com.techjar.vivecraftforge.entity.EntityVRObject;
import com.techjar.vivecraftforge.network.IPacket;
import com.techjar.vivecraftforge.proxy.ProxyServer;
import com.techjar.vivecraftforge.util.Quaternion;
import com.techjar.vivecraftforge.util.VRPlayerData;
import com.techjar.vivecraftforge.util.VivecraftReflector;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.Vec3;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PacketVRData implements IPacket {
	public int id;
	public Vec3 position;
	public float rotW, rotX, rotY, rotZ;
	public boolean handsSwapped;

	public PacketVRData() {
	}

	public PacketVRData(int id, Vec3 position, float rotW, float rotX, float rotY, float rotZ, boolean handsSwapped) {
		this.id = id;
		this.position = position;
		this.rotW = rotW;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.handsSwapped = handsSwapped;
	}

	@SideOnly(Side.CLIENT)
	public PacketVRData(int id, Vec3 position, Quaternion quat, boolean handsSwapped) {
		this(id, position, quat.w, quat.x, quat.y, quat.z, handsSwapped);
	}

	@Override
	public void encodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		buffer.writeByte(id | (handsSwapped ? 0x80 : 0));
		buffer.writeFloat((float)position.xCoord);
		buffer.writeFloat((float)position.yCoord);
		buffer.writeFloat((float)position.zCoord);
		buffer.writeFloat(rotW);
		buffer.writeFloat(rotX);
		buffer.writeFloat(rotY);
		buffer.writeFloat(rotZ);
	}

	@Override
	public void decodePacket(ChannelHandlerContext context, ByteBuf buffer) {
		int b = buffer.readUnsignedByte();
		id = b & 0x7F;
		handsSwapped = (b & 0x80) != 0;
		position = Vec3.createVectorHelper(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
		rotW = buffer.readFloat();
		rotX = buffer.readFloat();
		rotY = buffer.readFloat();
		rotZ = buffer.readFloat();
	}

	@Override
	public void handleClient(EntityPlayer player) {
	}

	@Override
	public void handleServer(EntityPlayer player) {
		VRPlayerData data = ProxyServer.vrPlayers.get(player);
		if (data != null && data.entities.size() >= 3) {
			data.handsSwapped = handsSwapped;
			EntityVRObject entity = data.entities.get(id);
			entity.setPosition(position.xCoord, position.yCoord, position.zCoord);
			entity.position.xCoord = position.xCoord;
			entity.position.yCoord = position.yCoord;
			entity.position.zCoord = position.zCoord;
			entity.rotW = rotW;
			entity.rotX = rotX;
			entity.rotY = rotY;
			entity.rotZ = rotZ;
			if (entity instanceof EntityVRArm) {
				((EntityVRArm)entity).mirror = handsSwapped;
			}
		}
	}
}
