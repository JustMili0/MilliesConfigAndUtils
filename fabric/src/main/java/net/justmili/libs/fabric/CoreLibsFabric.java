package net.justmili.libs.fabric;

import net.fabricmc.api.ModInitializer;
import net.justmili.libs.CoreLibs;
import net.justmili.libs.v1.config.sync.fabric.SyncConfigCSPNetworking;
import net.justmili.libs.v1.event.bridge.fabric.ServerEventsBridge;

public final class CoreLibsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SyncConfigCSPNetworking.registerServer();

        CoreLibs.init();

        ServerEventsBridge.init();
    }
}