package net.justmili.libs.fabric;

import net.fabricmc.api.ModInitializer;
import net.justmili.libs.CoreLibs;

public final class CoreLibsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        CoreLibs.init();
    }
}
