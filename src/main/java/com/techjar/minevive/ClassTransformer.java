package com.techjar.minevive;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer {
	@Override
	public byte[] transform(String arg0, String arg1, byte[] arg2) {
		//System.out.println("[MineViveForgeCore] " + arg0);
		if (arg0.equals("nh")) {
			System.out.println("[MineViveForgeCore] Found NetHandlerPlayServer: " + arg0);
			return patchClassASM(arg0, arg2, true);
		} else if (arg0.equals("net.minecraft.network.NetHandlerPlayServer")) {
			System.out.println("[MineViveForgeCore] Found NetHandlerPlayServer: " + arg0);
			return patchClassASM(arg0, arg2, false);
		} else if (arg0.equals("cpw.mods.fml.common.network.NetworkRegistry")) {
			System.out.println("[MineViveForgeCore] Found NetworkRegistry: " + arg0);
			return patchForgeClassASM(arg0, arg2);
		}
		return arg2;
	}
	
	public byte[] patchClassASM(String name, byte[] bytes, boolean obfuscated) {
		String methodName = null;
		String methodDesc = null;
		if (obfuscated) {
			methodName = "a";
			methodDesc = "(Ljd;)V";
		} else {
			methodName = "processPlayer";
			methodDesc = "(Lnet/minecraft/network/play/client/C03PacketPlayer;)V";
		}

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		Iterator<MethodNode> methods = classNode.methods.iterator();
		while (methods.hasNext()) {
			MethodNode m = methods.next();
			//System.out.println(m.name + m.desc);
			if (m.name.equals(methodName) && m.desc.equals(methodDesc)) {
				System.out.println("[MineViveForgeCore] Found method: " + methodName + methodDesc);
				for (int i = 0; i < m.instructions.size(); i++) {
					AbstractInsnNode insn = m.instructions.get(i);
					if (insn instanceof LdcInsnNode) {
						LdcInsnNode insn2 = (LdcInsnNode)insn;
						if (insn2.cst.getClass() == Double.class && (insn2.cst.equals(new Double("100.0")) || insn2.cst.equals(new Double("0.0625")))) {
							System.out.println("[MineViveForgeCore] Changing double constant " + insn2.cst + " to 10000");
							insn2.cst = new Double("10000.0");
						}
					}
				}
				break;
			}
		}

		// ASM specific for cleaning up and returning the final bytes for JVM processing.
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		classNode.accept(writer);
		/*try {
			FileOutputStream out = new FileOutputStream(new File("GameData.class"));
			out.write(writer.toByteArray());
			out.flush(); out.close();
		} catch (Exception ex) {}*/
		return writer.toByteArray();
	}
	
	public byte[] patchForgeClassASM(String name, byte[] bytes) {
		String methodName = "newChannel";
		String methodDesc = "(Ljava/lang/String;[Lio/netty/channel/ChannelHandler;)Ljava/util/EnumMap;";

		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		Iterator<MethodNode> methods = classNode.methods.iterator();
		while (methods.hasNext()) {
			MethodNode m = methods.next();
			//System.out.println(m.name + m.desc);
			if (m.name.equals(methodName) && m.desc.equals(methodDesc)) {
				System.out.println("[MineViveForgeCore] Found method: " + methodName + methodDesc);
				for (int i = 0; i < m.instructions.size(); i++) {
					AbstractInsnNode insn = m.instructions.get(i);
					if (insn instanceof LdcInsnNode) {
						LdcInsnNode insn2 = (LdcInsnNode)insn;
						if (insn2.cst.getClass() == String.class && insn2.cst.equals("MC|")) {
							int found = i - 1;
							System.out.println("[MineViveForgeCore] Found desired instruction node: " + insn2);
							for (int j = 0; j < 4; j++) {
								AbstractInsnNode insn3 = m.instructions.get(found);
								m.instructions.remove(insn3);
								System.out.println("[MineViveForgeCore] Removed instruction: " + insn3);
							}
							break;
						}
					}
				}
				break;
			}
		}

		// ASM specific for cleaning up and returning the final bytes for JVM processing.
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		/*try {
			FileOutputStream out = new FileOutputStream(new File("GameData.class"));
			out.write(writer.toByteArray());
			out.flush(); out.close();
		} catch (Exception ex) {}*/
		return writer.toByteArray();
	}
}
