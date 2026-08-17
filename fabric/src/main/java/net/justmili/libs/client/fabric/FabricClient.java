package net.justmili.libs.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.justmili.libs.client.CommonClient;
import net.justmili.libs.v1.config.sync.fabric.SyncConfigCSPNetworking;
import net.justmili.libs.v1.events.bridge.fabric.ClientEventsBridge;

public class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientEventsBridge.init();
        SyncConfigCSPNetworking.registerClient();

        CommonClient.init();
    }
}
