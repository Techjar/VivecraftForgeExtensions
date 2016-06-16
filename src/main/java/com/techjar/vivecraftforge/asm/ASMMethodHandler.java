package com.techjar.vivecraftforge.asm;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public interface ASMMethodHandler {
	public MethodTuple getDesiredMethod();
	public MethodNode patchMethod(ClassNode classNode, MethodNode methodNode, boolean obfuscated);
}
