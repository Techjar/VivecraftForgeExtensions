package com.techjar.vivecraftforge.core;

import java.io.File;
import java.util.Map;

import com.techjar.vivecraftforge.core.asm.ClassTransformer;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Name("VivecraftForgeCore")
@MCVersion("1.7.10")
@TransformerExclusions("com.techjar.vivecraftforge.core")
//@SortingIndex(1001)
public class FMLPlugin implements IFMLLoadingPlugin {
	public static File location;

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{ClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return VivecraftForgeCore.class.getName();
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
		location = (File)data.get("coremodLocation");
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}
}
