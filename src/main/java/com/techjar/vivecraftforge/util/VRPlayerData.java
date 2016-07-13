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
	public boolean newAPI;
	public boolean reverseHands;
	public float worldScale = 1;
	public boolean seated;
	
	public VRPlayerData copy() {
		VRPlayerData data = new VRPlayerData();
		data.newAPI = newAPI;
		data.reverseHands = reverseHands;
		data.worldScale = worldScale;
		data.seated = seated;
		data.entities = new ArrayList<EntityVRObject>(entities);
		data.entityIds = new ArrayList<Integer>(entityIds);
		return data;
	}
}
