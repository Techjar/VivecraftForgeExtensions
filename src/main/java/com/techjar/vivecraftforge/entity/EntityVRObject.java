package com.techjar.vivecraftforge.entity;

import com.techjar.vivecraftforge.util.Quaternion;

import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public abstract class EntityVRObject extends Entity implements IEntityAdditionalSpawnData {
	public Vec3 position = Vec3.createVectorHelper(0, 0, 0);
	public Vec3 positionLast = Vec3.createVectorHelper(0, 0, 0);
	public float rotW = 1, rotX, rotY, rotZ;
	public float rotWLast = 1, rotXLast, rotYLast, rotZLast;
	protected int associatedEntityId;
	protected EntityPlayer entityPlayer;
	private boolean spawned;
	
	public EntityVRObject(World world) {
		this(world, -1);
	}
	
	public EntityVRObject(World world, int associatedEntityId) {
		super(world);
		this.associatedEntityId = associatedEntityId;
		this.ignoreFrustumCheck = true;
		this.renderDistanceWeight = 10.0D;
	}

	@Override
	protected void entityInit() {
		dataWatcher.addObject(2, 0F);
		dataWatcher.addObject(3, 0F);
		dataWatcher.addObject(4, 0F);
		dataWatcher.addObject(5, 0F);
		dataWatcher.addObject(6, 0F);
		dataWatcher.addObject(7, 0F);
		dataWatcher.addObject(8, 0F);
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
			dataWatcher.updateObject(5, (float)rotW);
			dataWatcher.updateObject(6, (float)rotX);
			dataWatcher.updateObject(7, (float)rotY);
			dataWatcher.updateObject(8, (float)rotZ);
		}
		else {
			positionLast.xCoord = position.xCoord;
			positionLast.yCoord = position.yCoord;
			positionLast.zCoord = position.zCoord;
			rotWLast = rotW;
			rotXLast = rotX;
			rotYLast = rotY;
			rotZLast = rotZ;
			position.xCoord = dataWatcher.getWatchableObjectFloat(2);
			position.yCoord = dataWatcher.getWatchableObjectFloat(3);
			position.zCoord = dataWatcher.getWatchableObjectFloat(4);
			rotW = dataWatcher.getWatchableObjectFloat(5);
			rotX = dataWatcher.getWatchableObjectFloat(6);
			rotY = dataWatcher.getWatchableObjectFloat(7);
			rotZ = dataWatcher.getWatchableObjectFloat(8);
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
		buf.writeInt(associatedEntityId);
		buf.writeFloat((float)position.xCoord);
		buf.writeFloat((float)position.yCoord);
		buf.writeFloat((float)position.zCoord);
		buf.writeFloat((float)rotW);
		buf.writeFloat((float)rotX);
		buf.writeFloat((float)rotY);
		buf.writeFloat((float)rotZ);
	}

	@Override
	public void readSpawnData(ByteBuf buf) {
		associatedEntityId = buf.readInt();
		position.xCoord = positionLast.xCoord = buf.readFloat();
		position.yCoord = positionLast.yCoord = buf.readFloat();
		position.zCoord = positionLast.zCoord = buf.readFloat();
		rotW = rotWLast = buf.readFloat();
		rotX = rotXLast = buf.readFloat();
		rotY = rotYLast = buf.readFloat();
		rotZ = rotZLast = buf.readFloat();
	}
	
	@Override
	public void setPosition(double x, double y, double z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
	}

	public EntityPlayer getEntityPlayer() {
		if (entityPlayer == null) {
			entityPlayer = (EntityPlayer)worldObj.getEntityByID(associatedEntityId);
		}
		return entityPlayer;
	}
	
	public boolean isSpawned() {
		return spawned;
	}
	
	public void setSpawned() {
		spawned = true;
	}
	
	@SideOnly(Side.CLIENT)
	public Quaternion getRotation() {
		return new Quaternion(rotW, rotX, rotY, rotZ);
	}
	
	@SideOnly(Side.CLIENT)
	public Quaternion getRotationLast() {
		return new Quaternion(rotWLast, rotXLast, rotYLast, rotZLast);
	}
}
