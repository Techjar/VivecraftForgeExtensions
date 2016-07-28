package com.techjar.vivecraftforge.core.asm.handler;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
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

public class ASMHandlerIncreaseReachDistance extends ASMClassHandler {
	@Override
	public ClassTuple getDesiredClass() {
		return new ClassTuple("net.minecraft.network.NetHandlerPlayServer", "nh");
	}

	@Override
	public ASMMethodHandler[] getMethodHandlers() {
		return new ASMMethodHandler[]{new PlacementMethodHandler(), new DiggingMethodHandler(), new EntityMethodHandler()};
	}

	@Override
	public boolean getComputeFrames() {
		return false;
	}
	
	public static class PlacementMethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("processPlayerBlockPlacement", "(Lnet/minecraft/network/play/client/C08PacketPlayerBlockPlacement;)V", "a", "(Ljo;)V");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			AbstractInsnNode insert = ASMUtil.findFirstInstruction(methodNode, Opcodes.DSTORE, 10);
			if (insert == null) return;
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? "nh" : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "playerEntity", obfuscated ? "Lmw;" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
			insnList.add(new VarInsnNode(Opcodes.DLOAD, 10));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "playerBlockReachDistance", obfuscated ? "(Lyz;D)D" : "(Lnet/minecraft/entity/player/EntityPlayer;D)D", false));
			insnList.add(new VarInsnNode(Opcodes.DSTORE, 10));
			methodNode.instructions.insert(insert, insnList);
			VivecraftForgeLog.debug("Inserted delegate method call.");
		}
	}
	
	public static class DiggingMethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("processPlayerDigging", "(Lnet/minecraft/network/play/client/C07PacketPlayerDigging;)V", "a", "(Lji;)V");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			AbstractInsnNode insert = ASMUtil.findFirstInstruction(methodNode, Opcodes.DSTORE, 15);
			if (insert == null) return;
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? "nh" : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "playerEntity", obfuscated ? "Lmw;" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
			insnList.add(new VarInsnNode(Opcodes.DLOAD, 15));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "playerBlockReachDistance", obfuscated ? "(Lyz;D)D" : "(Lnet/minecraft/entity/player/EntityPlayer;D)D", false));
			insnList.add(new VarInsnNode(Opcodes.DSTORE, 15));
			methodNode.instructions.insert(insert, insnList);
			VivecraftForgeLog.debug("Inserted delegate method call.");
		}
	}
	
	public static class EntityMethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("processUseEntity", "(Lnet/minecraft/network/play/client/C02PacketUseEntity;)V", "a", "(Lja;)V");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			AbstractInsnNode insert = ASMUtil.findFirstInstruction(methodNode, Opcodes.DSTORE, 5);
			if (insert == null) return;
			InsnList insnList = new InsnList();
			insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
			insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? "nh" : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "playerEntity", obfuscated ? "Lmw;" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
			insnList.add(new VarInsnNode(Opcodes.DLOAD, 5));
			insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "playerEntityReachDistance", obfuscated ? "(Lyz;D)D" : "(Lnet/minecraft/entity/player/EntityPlayer;D)D", false));
			insnList.add(new VarInsnNode(Opcodes.DSTORE, 5));
			methodNode.instructions.insert(insert, insnList);
			VivecraftForgeLog.debug("Inserted delegate method call.");
			
			AbstractInsnNode removeInsn = ASMUtil.findFirstInstruction(methodNode, Opcodes.LDC, 9.0D);
			if (removeInsn != null) {
				int remove = methodNode.instructions.indexOf(removeInsn);
				for (int i = 0; i < 2; i++) {
					methodNode.instructions.remove(methodNode.instructions.get(remove));
				}
				VivecraftForgeLog.debug("Removed variable assignment.");
			} else {
				VivecraftForgeLog.debug("Variable assignment not found.");
			}
		}
	}
}
