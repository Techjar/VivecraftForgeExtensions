package com.techjar.vivecraftforge.core.asm.handler;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.techjar.vivecraftforge.core.asm.ASMClassHandler;
import com.techjar.vivecraftforge.core.asm.ASMMethodHandler;
import com.techjar.vivecraftforge.core.asm.ClassTuple;
import com.techjar.vivecraftforge.core.asm.MethodTuple;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;

public class ASMHandlerPlayerScaling extends ASMClassHandler {
	@Override
	public ClassTuple getDesiredClass() {
		return new ClassTuple("net.minecraft.client.renderer.entity.RendererLivingEntity", "boh");
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
			return new MethodTuple("doRender", "(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", "a", "(Lsv;DDDFF)V");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			for (int i = 0; i < methodNode.instructions.size(); i++) {
				AbstractInsnNode insn = methodNode.instructions.get(i);
				if (insn instanceof MethodInsnNode) {
					MethodInsnNode insn2 = (MethodInsnNode)insn;
					if (insn2.owner.equals("org/lwjgl/opengl/GL11") && insn2.name.equals("glScalef")) {
						InsnList insnList = new InsnList();
						insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
						insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "scalePlayer", "(Lnet/minecraft/entity/Entity;)V", false));
						methodNode.instructions.insert(insn2, insnList);
						VivecraftForgeLog.debug("Inserted delegate method call.");
						break;
					}
				}
			}
		}
	}
}
