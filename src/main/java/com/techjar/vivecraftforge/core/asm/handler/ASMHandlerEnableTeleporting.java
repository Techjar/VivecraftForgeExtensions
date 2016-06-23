package com.techjar.vivecraftforge.core.asm.handler;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.techjar.vivecraftforge.core.asm.ASMClassHandler;
import com.techjar.vivecraftforge.core.asm.ASMMethodHandler;
import com.techjar.vivecraftforge.core.asm.ClassTuple;
import com.techjar.vivecraftforge.core.asm.MethodTuple;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;

public class ASMHandlerEnableTeleporting extends ASMClassHandler {
	@Override
	public ClassTuple getDesiredClass() {
		return new ClassTuple("net.minecraft.network.NetHandlerPlayServer", "nh");
	}

	@Override
	public ASMMethodHandler[] getMethodHandlers() {
		return new ASMMethodHandler[]{new MethodHandler()};
	}

	@Override
	public boolean getComputeFrames() {
		return false;
	}

	public static class MethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("processPlayer", "(Lnet/minecraft/network/play/client/C03PacketPlayer;)V", "a", "(Ljd;)V");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			for (int i = 0; i < methodNode.instructions.size(); i++) {
				AbstractInsnNode insn = methodNode.instructions.get(i);
				if (insn instanceof LdcInsnNode) {
					LdcInsnNode insn2 = (LdcInsnNode)insn;
					if (insn2.cst.getClass() == Double.class && (insn2.cst.equals(new Double("100.0")) || insn2.cst.equals(new Double("0.0625")))) {
						VivecraftForgeLog.debug("Changing double constant " + insn2.cst + " to 10000");
						insn2.cst = new Double("10000.0");
					}
				}
			}
		}
	}
}
