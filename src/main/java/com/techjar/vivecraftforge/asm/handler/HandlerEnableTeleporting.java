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

public class HandlerEnableTeleporting extends ASMClassHandler {
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
	
	/*@Override
	public byte[] patchClass(byte[] bytes, boolean obfuscated) {
		try { // Stupid shit to only patch if we're on a server
			Class.forName("net.minecraft.client.renderer.entity.RenderPlayer", false, null);
		} catch (ClassNotFoundException ex) {
			try {
				Class.forName("bop", false, null);
			} catch (ClassNotFoundException ex2) {
				return super.patchClass(bytes, obfuscated);
			}
		}
		return bytes;
	}*/

	public static class MethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("processPlayer", "(Lnet/minecraft/network/play/client/C03PacketPlayer;)V", "a", "(Ljd;)V");
		}

		@Override
		public MethodNode patchMethod(ClassNode classNode, MethodNode methodNode, boolean obfuscated) {
			for (int i = 0; i < methodNode.instructions.size(); i++) {
				AbstractInsnNode insn = methodNode.instructions.get(i);
				if (insn instanceof LdcInsnNode) {
					LdcInsnNode insn2 = (LdcInsnNode)insn;
					if (insn2.cst.getClass() == Double.class && (insn2.cst.equals(new Double("100.0")) || insn2.cst.equals(new Double("0.0625")))) {
						VivecraftForgeLog.info("Changing double constant " + insn2.cst + " to 10000");
						insn2.cst = new Double("10000.0");
					}
				}
			}
			return methodNode;
		}
	}
}
