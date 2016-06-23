package com.techjar.vivecraftforge.core.asm.handler;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import com.techjar.vivecraftforge.core.asm.ASMClassHandler;
import com.techjar.vivecraftforge.core.asm.ASMMethodHandler;
import com.techjar.vivecraftforge.core.asm.ClassTuple;
import com.techjar.vivecraftforge.core.asm.MethodTuple;
import com.techjar.vivecraftforge.util.Util;
import com.techjar.vivecraftforge.util.VivecraftForgeLog;

import cpw.mods.fml.common.FMLCommonHandler;

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
			for (int i = 0; i < methodNode.instructions.size(); i++) {
				AbstractInsnNode insn = methodNode.instructions.get(i);
				if (insn instanceof VarInsnNode) {
					VarInsnNode insn2 = (VarInsnNode)insn;
					if (insn2.getOpcode() == Opcodes.DSTORE && insn2.var == 10) {
						InsnList insnList = new InsnList();
						insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
						insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? "nh" : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "playerEntity", obfuscated ? "Lmw;" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
						insnList.add(new VarInsnNode(Opcodes.DLOAD, 10));
						insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "playerBlockReachDistance", obfuscated ? "(Lyz;D)D" : "(Lnet/minecraft/entity/player/EntityPlayer;D)D", false));
						insnList.add(new VarInsnNode(Opcodes.DSTORE, 10));
						methodNode.instructions.insert(insn2, insnList);
						VivecraftForgeLog.debug("Inserted delegate method call.");
						break;
					}
				}
			}
		}
	}
	
	public static class DiggingMethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("processPlayerDigging", "(Lnet/minecraft/network/play/client/C07PacketPlayerDigging;)V", "a", "(Lji;)V");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			for (int i = 0; i < methodNode.instructions.size(); i++) {
				AbstractInsnNode insn = methodNode.instructions.get(i);
				if (insn instanceof VarInsnNode) {
					VarInsnNode insn2 = (VarInsnNode)insn;
					if (insn2.getOpcode() == Opcodes.DSTORE && insn2.var == 15) {
						InsnList insnList = new InsnList();
						insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
						insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? "nh" : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "playerEntity", obfuscated ? "Lmw;" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
						insnList.add(new VarInsnNode(Opcodes.DLOAD, 15));
						insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "playerBlockReachDistance", obfuscated ? "(Lyz;D)D" : "(Lnet/minecraft/entity/player/EntityPlayer;D)D", false));
						insnList.add(new VarInsnNode(Opcodes.DSTORE, 15));
						methodNode.instructions.insert(insn2, insnList);
						VivecraftForgeLog.debug("Inserted delegate method call.");
						break;
					}
				}
			}
		}
	}
	
	public static class EntityMethodHandler implements ASMMethodHandler {
		@Override
		public MethodTuple getDesiredMethod() {
			return new MethodTuple("processUseEntity", "(Lnet/minecraft/network/play/client/C02PacketUseEntity;)V", "a", "(Lja;)V");
		}

		@Override
		public void patchMethod(MethodNode methodNode, ClassNode classNode, boolean obfuscated) {
			boolean insertedDelegate = false;
			for (int i = 0; i < methodNode.instructions.size(); i++) {
				AbstractInsnNode insn = methodNode.instructions.get(i);
				if (!insertedDelegate && insn instanceof VarInsnNode) {
					VarInsnNode insn2 = (VarInsnNode)insn;
					if (insn2.getOpcode() == Opcodes.DSTORE && insn2.var == 5) {
						InsnList insnList = new InsnList();
						insnList.add(new VarInsnNode(Opcodes.ALOAD, 0));
						insnList.add(new FieldInsnNode(Opcodes.GETFIELD, obfuscated ? "nh" : "net/minecraft/network/NetHandlerPlayServer", obfuscated ? "b" : "playerEntity", obfuscated ? "Lmw;" : "Lnet/minecraft/entity/player/EntityPlayerMP;"));
						insnList.add(new VarInsnNode(Opcodes.DLOAD, 5));
						insnList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "com/techjar/vivecraftforge/util/ASMDelegator", "playerEntityReachDistance", obfuscated ? "(Lyz;D)D" : "(Lnet/minecraft/entity/player/EntityPlayer;D)D", false));
						insnList.add(new VarInsnNode(Opcodes.DSTORE, 5));
						methodNode.instructions.insert(insn2, insnList);
						insertedDelegate = true;
						VivecraftForgeLog.debug("Inserted delegate method call.");
					}
				} else if (insn instanceof LdcInsnNode) {
					LdcInsnNode insn2 = (LdcInsnNode)insn;
					if (insn2.cst.getClass() == Double.class && insn2.cst.equals(new Double("9.0"))) {
						methodNode.instructions.remove(insn2);
						methodNode.instructions.remove(methodNode.instructions.get(i));
						VivecraftForgeLog.debug("Removed variable assignment.");
						break;
					}
				}
			}
		}
	}
}
