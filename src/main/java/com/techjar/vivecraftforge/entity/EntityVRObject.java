package com.techjar.vivecraftforge.entity;

import sun.misc.Unsafe;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityVRObject extends Entity implements IEntityAdditionalSpawnData {
	public Vec3 position = Vec3.createVectorHelper(0, 0, 0);
	public Vec3 aimVector = Vec3.createVectorHelper(0, 0, 0);
	public Vec3 positionLast = Vec3.createVectorHelper(0, 0, 0);
	public Vec3 aimVectorLast = Vec3.createVectorHelper(0, 0, 0);
	protected int associatedEntityId;
	protected EntityPlayer entityPlayer;
	
	public EntityVRObject(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(2, 0);
		dataWatcher.addObject(3, 0);
		dataWatcher.addObject(4, 0);
		dataWatcher.addObject(5, 0);
		dataWatcher.addObject(6, 0);
		dataWatcher.addObject(7, 0);
	}
	
	@Override
	public void onUpdate() {
		if (!worldObj.isRemote) {
			if (getEntityPlayer() == null || getEntityPlayer().isDead) {
				this.setDead();
				return;
			}
			dataWatcher.updateObject(2, (float)position.xCoord);
			dataWatcher.updateObject(3, (float)position.yCoord);
			dataWatcher.updateObject(4, (float)position.zCoord);
			dataWatcher.updateObject(5, (float)aimVector.xCoord);
			dataWatcher.updateObject(6, (float)aimVector.yCoord);
			dataWatcher.updateObject(7, (float)aimVector.zCoord);
		}
		else {
			position.xCoord = dataWatcher.getWatchableObjectFloat(2);
			position.yCoord = dataWatcher.getWatchableObjectFloat(3);
			position.zCoord = dataWatcher.getWatchableObjectFloat(4);
			aimVector.xCoord = dataWatcher.getWatchableObjectFloat(5);
			aimVector.yCoord = dataWatcher.getWatchableObjectFloat(6);
			aimVector.zCoord = dataWatcher.getWatchableObjectFloat(7);
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {
	}
	
	@Override
	public void writeSpawnData(ByteBuf buf) {
		buf.writeFloat((float)position.xCoord);
		buf.writeFloat((float)position.yCoord);
		buf.writeFloat((float)position.zCoord);
		buf.writeFloat((float)aimVector.xCoord);
		buf.writeFloat((float)aimVector.yCoord);
		buf.writeFloat((float)aimVector.zCoord);
		buf.writeInt(associatedEntityId);
	}

	@Override
	public void readSpawnData(ByteBuf buf) {
		position.xCoord = positionLast.xCoord = buf.readFloat();
		position.yCoord = positionLast.yCoord = buf.readFloat();
		position.zCoord = positionLast.zCoord = buf.readFloat();
		aimVector.xCoord = aimVectorLast.xCoord = buf.readFloat();
		aimVector.yCoord = aimVectorLast.yCoord = buf.readFloat();
		aimVector.zCoord = aimVectorLast.zCoord = buf.readFloat();
		associatedEntityId = buf.readInt();
	}
	
	public EntityPlayer getEntityPlayer() {
		if (entityPlayer == null) {
			entityPlayer = (EntityPlayer)worldObj.getEntityByID(associatedEntityId);
		}
		return entityPlayer;
	}
}
