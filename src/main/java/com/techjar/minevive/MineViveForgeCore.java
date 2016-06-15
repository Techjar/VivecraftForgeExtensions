package com.techjar.minevive;

import java.util.Arrays;

import net.minecraftforge.common.config.Configuration;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class MineViveForgeCore extends DummyModContainer {
	public MineViveForgeCore() {
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId = "MineViveForgeCore";
		meta.name = "MineViveForgeCore";
		meta.version = "@VERSION@"; //String.format("%d.%d.%d.%d", majorVersion, minorVersion, revisionVersion, buildVersion);
		meta.credits = "blah";
		meta.authorList = Arrays.asList("Techjar");
		meta.description = "";
		meta.url = "";
		meta.updateUrl = "";
		meta.screenshots = new String[0];
		meta.logoFile = "";
	}
	
	@Override
    public boolean registerBus(EventBus bus, LoadController controller) {
		bus.register(this);
		return true;
    }
}
