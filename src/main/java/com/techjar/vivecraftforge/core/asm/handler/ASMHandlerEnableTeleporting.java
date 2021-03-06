package com.techjar.vivecraftforge.core.asm.handler;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.techjar.vivecraftforge.core.asm.ASMClassHandler;
import com.techjar.vivecraftforge.core.asm.ASMMethodHandler;
import com.techjar.vivecraftforge.core.asm.ASMUtil;
import com.techjar.vivecraftforge.core.asm.ClassTuple;
import com.techjar.vivecraftforge.core.asm.MethodTuple;
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
			LdcInsnNode ldc1 = (LdcInsnNode)ASMUtil.findFirstInstruction(methodNode, Opcodes.LDC, 100D);
			VivecraftForgeLog.debug("Changing double constant " + ldc1.cst + " to 10000");
			ldc1.cst = 10000D;
			LdcInsnNode ldc2 = (LdcInsnNode)ASMUtil.findFirstInstruction(methodNode, Opcodes.LDC, 0.0625D);
			VivecraftForgeLog.debug("Changing double constant " + ldc2.cst + " to 10000");
			ldc2.cst = 10000D;
		}
	}
}
