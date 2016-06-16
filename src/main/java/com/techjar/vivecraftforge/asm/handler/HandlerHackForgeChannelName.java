package com.techjar.vivecraftforge.asm.handler;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.techjar.vivecraftforge.asm.ASMClassHandler;
import com.techjar.vivecraftforge.asm.ASMMethodHandler;
import com.techjar.vivecraftforge.asm.ClassTuple;
import com.techjar.vivecraftforge.asm.MethodTuple;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;

public class HandlerHackForgeChannelName extends ASMClassHandler {
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
		public MethodNode patchMethod(ClassNode classNode, MethodNode methodNode, boolean obfuscated) {
			for (int i = 0; i < methodNode.instructions.size(); i++) {
				AbstractInsnNode insn = methodNode.instructions.get(i);
				if (insn instanceof LdcInsnNode) {
					LdcInsnNode insn2 = (LdcInsnNode)insn;
					if (insn2.cst.getClass() == String.class && insn2.cst.equals("MC|")) {
						int found = i - 1;
						VivecraftForgeLog.info("Found desired instruction node: " + insn2);
						for (int j = 0; j < 4; j++) {
							AbstractInsnNode insn3 = methodNode.instructions.get(found);
							methodNode.instructions.remove(insn3);
							VivecraftForgeLog.info("Removed instruction: " + insn3);
						}
						break;
					}
				}
			}
			return methodNode;
		}
	}
}
