package com.techjar.vivecraftforge.asm.handler;

import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;

import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.techjar.vivecraftforge.asm.ASMClassHandler;
import com.techjar.vivecraftforge.asm.ASMMethodHandler;
import com.techjar.vivecraftforge.asm.ClassTuple;
import com.techjar.vivecraftforge.asm.MethodTuple;
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
						//GL11.glScalef(0.5F, 0.5F, 0.5F);
						insnList.add(new LdcInsnNode(new Float("0.5")));
						insnList.add(new LdcInsnNode(new Float("0.5")));
						insnList.add(new LdcInsnNode(new Float("0.5")));
						insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "org/lwjgl/opengl/GL11", "glScalef", "(FFF)V", false));
						methodNode.instructions.insert(insn2, insnList);
						break;
					}
				}
			}
		}
	}
}
