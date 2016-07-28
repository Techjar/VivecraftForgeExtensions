package com.techjar.vivecraftforge.core.asm;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Type;

import com.techjar.vivecraftforge.core.asm.handler.*;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;

import net.minecraft.entity.EntityLiving;
import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
	private static final List<ASMClassHandler> asmHandlers = new ArrayList<ASMClassHandler>();
	static {
		asmHandlers.add(new ASMHandlerEnableTeleporting());
		asmHandlers.add(new ASMHandlerHackForgeChannelName());
		asmHandlers.add(new ASMHandlerPlayerScaling());
		asmHandlers.add(new ASMHandlerIncreaseReachDistance());
		asmHandlers.add(new ASMHandlerCreeperRadius());
	}
	
	@Override
	public byte[] transform(String name, String transformedName, byte[] bytes) {
		for (ASMClassHandler handler : asmHandlers) {
			if (!handler.shouldPatchClass()) continue;
			ClassTuple tuple = handler.getDesiredClass();
			if (name.equals(tuple.classNameObf)) {
				VivecraftForgeLog.debug("Patching class: " + name + " (" + tuple.className + ")");
				bytes = handler.patchClass(bytes, true);
			} else if (name.equals(tuple.className)) {
				VivecraftForgeLog.debug("Patching class: " + name);
				bytes = handler.patchClass(bytes, false);
			}
		}
		return bytes;
	}
}
