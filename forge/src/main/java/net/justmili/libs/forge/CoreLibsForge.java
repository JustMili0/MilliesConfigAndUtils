package net.justmili.libs.forge;

import net.justmili.libs.CoreLibs;
import net.justmili.libs.v1.events.bridge.forge.ServerEventsBridge;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CoreLibs.MODID)
public final class CoreLibsForge {
    public static IEventBus EVENT_BUS;

    public CoreLibsForge(FMLJavaModLoadingContext modContext) {
        MinecraftForge.EVENT_BUS.register(this);
        EVENT_BUS = modContext.getModEventBus();
        ServerEventsBridge.init();
        // TODO: Add SyncConfigCSPNetworking for Forge (Server)

        CoreLibs.init();
    }

    @SubscribeEvent
    static void test(PlayerEvent event) {
    }
}