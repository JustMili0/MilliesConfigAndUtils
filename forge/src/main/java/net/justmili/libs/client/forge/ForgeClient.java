package net.justmili.libs.client.forge;

import net.justmili.libs.client.CommonClient;
import net.justmili.libs.v1.events.bridge.forge.ClientEventsBridge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ForgeClient {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        ClientEventsBridge.init();
        // TODO: Add SyncConfigCSPNetworking for Forge (Client)

        CommonClient.init();
    }
}
