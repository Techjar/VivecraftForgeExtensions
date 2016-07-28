package com.techjar.vivecraftforge.core.asm.handler;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.techjar.vivecraftforge.core.asm.ASMClassHandler;
import com.techjar.vivecraftforge.core.asm.ASMMethodHandler;
import com.techjar.vivecraftforge.core.asm.ASMUtil;
import com.techjar.vivecraftforge.core.asm.ClassTuple;
import com.techjar.vivecraftforge.core.asm.MethodTuple;
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
			LdcInsnNode removeInsn = (LdcInsnNode)ASMUtil.findFirstInstruction(methodNode, Opcodes.LDC, "MC|");
			VivecraftForgeLog.debug("Found desired instruction node: " + removeInsn.getClass().getSimpleName() + " " + removeInsn.cst);
			int remove = methodNode.instructions.indexOf(removeInsn) - 1;
			for (int j = 0; j < 4; j++) {
				AbstractInsnNode insn = methodNode.instructions.get(remove);
				methodNode.instructions.remove(insn);
				VivecraftForgeLog.debug("Removed instruction: " + insn.getClass().getSimpleName());
			}
		}
	}
}
