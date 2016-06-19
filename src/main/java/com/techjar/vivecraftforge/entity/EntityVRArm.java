package com.techjar.vivecraftforge.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;

public abstract class EntityVRArm extends EntityVRObject {
	public boolean mirror;
	
	public EntityVRArm(World world) {
		super(world);
	}

	@Override
	protected void entityInit() {
		super.entityInit();
		dataWatcher.addObject(9, (byte)0);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		if (!worldObj.isRemote) {
			dataWatcher.updateObject(9, mirror ? (byte)1 : (byte)0);
		}
		else {
			mirror = dataWatcher.getWatchableObjectByte(9) != 0;
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buf) {
		super.writeSpawnData(buf);
		buf.writeBoolean(mirror);
	}

	@Override
	public void readSpawnData(ByteBuf buf) {
		super.readSpawnData(buf);
		mirror = buf.readBoolean();
	}
}
