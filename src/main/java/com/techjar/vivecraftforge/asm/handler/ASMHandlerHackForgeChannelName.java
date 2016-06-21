package com.techjar.vivecraftforge.asm.handler;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.techjar.vivecraftforge.asm.ASMClassHandler;
import com.techjar.vivecraftforge.asm.ASMMethodHandler;
import com.techjar.vivecraftforge.asm.ClassTuple;
import com.techjar.vivecraftforge.asm.MethodTuple;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;

public class ASMHandlerHackForgeChannelName extends ASMClassHandler {
	@Override
	public ClassTuple getDesiredClass() {
		return new ClassTuple("cpw.mods.fml.common.network.NetworkRegistry");
	}

	@Override
	public ASMMethodHandler[] getMethodHandlers() {
		return new ASMMethodHandler[]{new MethodHandler()};
	}

	@Override
	public boolean getComputeFrames() {
		return true;
	}
	
	public static class MethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("newChannel", "(Ljava/lang/String;[Lio/netty/channel/ChannelHandler;)Ljava/util/EnumMap;");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			for (int i = 0; i < methodNode.instructions.size(); i++) {
				AbstractInsnNode insn = methodNode.instructions.get(i);
				if (insn instanceof LdcInsnNode) {
					LdcInsnNode insn2 = (LdcInsnNode)insn;
					if (insn2.cst.getClass() == String.class && insn2.cst.equals("MC|")) {
						int found = i - 1;
						VivecraftForgeLog.debug("Found desired instruction node: " + insn2.getClass().getSimpleName() + " " + insn2.cst);
						for (int j = 0; j < 4; j++) {
							AbstractInsnNode insn3 = methodNode.instructions.get(found);
							methodNode.instructions.remove(insn3);
							VivecraftForgeLog.debug("Removed instruction: " + insn3.getClass().getSimpleName());
						}
						break;
					}
				}
			}
		}
	}
}
