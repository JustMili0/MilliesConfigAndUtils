package net.justmili.corelibs.fabric;

import net.fabricmc.api.ModInitializer;
import net.justmili.api.events.bridge.fabric.ServerEventsBridge;
import net.justmili.corelibs.CoreLibs;
import net.justmili.corelibs.v1.config.sync.fabric.SyncConfigCSPNetworking;

public final class CoreLibsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ServerEventsBridge.init();
        SyncConfigCSPNetworking.registerServer();

        CoreLibs.init();
    }
}