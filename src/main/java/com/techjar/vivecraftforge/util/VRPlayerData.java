package com.techjar.vivecraftforge.util;

import java.util.ArrayList;
import java.util.List;

import com.techjar.vivecraftforge.entity.EntityVRObject;

public class VRPlayerData {
	/**
	 * Only on server
	 */
	public List<EntityVRObject> entities = new ArrayList<EntityVRObject>();
	/**
	 * Only on client
	 */
	public List<Integer> entityIds = new ArrayList<Integer>();
	/**
	 * Not guaranteed to be up-to-date on client, don't rely on it there
	 */
	public boolean handsSwapped;
}
