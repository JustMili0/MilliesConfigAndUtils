package net.justmili.libs.fabric;

import net.fabricmc.api.ModInitializer;
import net.justmili.libs.CoreLibs;
import net.justmili.libs.v1.config.sync.fabric.SyncConfigCSPNetworking;
import net.justmili.libs.v1.events.bridge.fabric.ServerEventsBridge;

public final class CoreLibsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerEventsBridge.init();
        SyncConfigCSPNetworking.registerServer();

        CoreLibs.init();
    }
}