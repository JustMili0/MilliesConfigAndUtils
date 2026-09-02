package net.justmili.corelibs.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.justmili.api.events.bridge.fabric.ClientEventsBridge;
import net.justmili.corelibs.client.CommonClient;
import net.justmili.corelibs.v1.config.sync.fabric.SyncConfigCSPNetworking;

public class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEventsBridge.init();
        SyncConfigCSPNetworking.registerClient();

        CommonClient.init();
    }
}
