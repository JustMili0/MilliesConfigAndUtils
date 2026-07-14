package net.justmili.libs.v1.event.bridge.forge;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;

import static net.justmili.libs.v1.event.server.ServerLifecycleEvents.*;
import static net.justmili.libs.v1.event.server.ServerTickEvents.*;

public final class ServerEventsBridge {
    private ServerEventsBridge() { }

    public static void init() {
        // Server Lifecycle
        MinecraftForge.EVENT_BUS.addListener((ServerStartingEvent event) -> STARTING.invoker().onServerStarting(event.getServer()));
        MinecraftForge.EVENT_BUS.addListener((ServerStoppingEvent event) -> STOPPING.invoker().onServerStopping(event.getServer()));
        MinecraftForge.EVENT_BUS.addListener((ServerStoppedEvent event) -> STOPPED.invoker().onServerStopped(event.getServer()));
        MinecraftForge.EVENT_BUS.addListener((OnDatapackSyncEvent event) -> {
            var playerList = event.getPlayerList();
            var player = event.getPlayer();
            if (player != null) {
                DATAPACK_SYNC_CONTENTS.invoker().onDatapackSyncContents(player, true);
            } else {
                for (var serverPlayer : playerList.getPlayers()) {
                    DATAPACK_SYNC_CONTENTS.invoker().onDatapackSyncContents(serverPlayer, false);
                }
            }
        });

        // Connection
        // TODO: add

        // Server Ticks
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent event) -> {
            if (isPre(event)) SERVER_PRE.invoker().onStartTick(event.getServer());
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.ServerTickEvent event) -> {
            if (isPost(event)) SERVER_POST.invoker().onEndTick(event.getServer());
        });

        MinecraftForge.EVENT_BUS.addListener((TickEvent.LevelTickEvent event) -> {
            if (!isPre(event)) return;
            if (!(event.level instanceof ServerLevel level)) return;

            LEVEL_PRE.invoker().onStartTick(level); // Tick level
            for (var entity : level.getAllEntities()) ENTITY_PRE.invoker().onStartTick(entity); // Tick entities
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.LevelTickEvent event) -> {
            if (!isPost(event)) return;
            if (!(event.level instanceof ServerLevel level)) return;

            LEVEL_POST.invoker().onEndTick(level); // Tick level
            for (var entity : level.getAllEntities()) ENTITY_POST.invoker().onEndTick(entity); // Tick entities
        });

        MinecraftForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent event) -> {
            if (isPre(event)) PLAYER_PRE.invoker().onStartTick(event.player);
        });
        MinecraftForge.EVENT_BUS.addListener((TickEvent.PlayerTickEvent event) -> {
            if (isPost(event)) PLAYER_POST.invoker().onEndTick(event.player);
        });
    }

    private static boolean isPre(TickEvent event) {
        return event.phase == TickEvent.Phase.START;
    }
    private static boolean isPost(TickEvent event) {
        return event.phase == TickEvent.Phase.END;
    }
}