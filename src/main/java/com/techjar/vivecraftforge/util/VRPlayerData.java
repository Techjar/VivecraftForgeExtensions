package com.techjar.vivecraftforge.util;

import java.util.ArrayList;
import java.util.List;

import com.techjar.vivecraftforge.entity.EntityVRObject;

public class VRPlayerData {
	/**
	 * Only on server
	 */
	public List<EntityVRObject> entities = new ArrayList<EntityVRObject>();
	public List<Integer> entityIds = new ArrayList<Integer>();
	/**
	 * Not guaranteed to be up-to-date on client, don't rely on it there
	 */
	public boolean handsSwapped;
	
	public VRPlayerData copy() {
		VRPlayerData data = new VRPlayerData();
		data.handsSwapped = handsSwapped;
		data.entities = new ArrayList<EntityVRObject>(entities);
		data.entityIds = new ArrayList<Integer>(entityIds);
		return data;
	}
}
