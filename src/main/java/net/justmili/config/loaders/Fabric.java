package net.justmili.config.loaders;

import net.fabricmc.api.ModInitializer;

import net.justmili.config.ConfigLib;

public class Fabric implements ModInitializer {
	@Override
	public void onInitialize() {
        ConfigLib.init();
	}
}