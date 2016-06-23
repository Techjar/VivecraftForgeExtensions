package com.techjar.vivecraftforge.core.asm;

public class ClassTuple {
	public final String className;
	public final String classNameObf;
	
	public ClassTuple(String className, String classNameObf) {
		this.className = className;
		this.classNameObf = classNameObf;
	}
	
	public ClassTuple(String className) {
		this(className, null);
	}
}
