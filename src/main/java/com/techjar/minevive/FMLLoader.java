package com.techjar.minevive;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Name("MineViveForgeCore")
@MCVersion("1.7.10")
@TransformerExclusions("com.techjar.minevive")
//@SortingIndex(1001)
public class FMLLoader implements IFMLLoadingPlugin {
	public static File location;

	@Override
	public String[] getASMTransformerClass() {
		return new String[]{ClassTransformer.class.getName()};
	}

	@Override
	public String getModContainerClass() {
		return MineViveForgeCore.class.getName();
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
