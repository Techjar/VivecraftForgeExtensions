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
import com.techjar.vivecraftforge.core.asm.ASMUtil;
import com.techjar.vivecraftforge.core.asm.ClassTuple;
import com.techjar.vivecraftforge.core.asm.MethodTuple;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;

public class ASMHandlerEndermanLook extends ASMClassHandler {
	@Override
	public ClassTuple getDesiredClass() {
		return new ClassTuple("net.minecraft.entity.monster.EntityEnderman", "ya");
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
			return new MethodTuple("shouldAttackPlayer", "(Lnet/minecraft/entity/player/EntityPlayer;)Z", "f", "(Lyz;)Z");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			AbstractInsnNode insert = ASMUtil.findFirstInstruction(methodNode, Opcodes.INVOKEVIRTUAL, obfuscated ? "azw" : "net/minecraft/util/Vec3", obfuscated ? "a" : "normalize", obfuscated ? "()Lazw;" : "()Lnet/minecraft/util/Vec3;", false);
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 1));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "endermanLook", obfuscated ? "(Lazw;Lyz;)Lazw;" : "(Lnet/minecraft/util/Vec3;Lnet/minecraft/entity/player/EntityPlayer;)Lnet/minecraft/util/Vec3;", false));
			methodNode.instructions.insert(insert, insnList);
			VivecraftForgeLog.debug("Inserted delegate method call.");
		}
	}
}
