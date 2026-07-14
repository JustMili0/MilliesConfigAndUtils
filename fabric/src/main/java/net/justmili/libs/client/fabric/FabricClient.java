package net.justmili.libs.client.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.justmili.libs.client.CommonClient;
import net.justmili.libs.v1.config.sync.fabric.SyncConfigCSPNetworking;

public class FabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        SyncConfigCSPNetworking.registerClient();

        CommonClient.init();
    }
}
