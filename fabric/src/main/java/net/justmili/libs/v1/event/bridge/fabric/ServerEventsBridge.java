package net.justmili.libs.v1.event.bridge.fabric;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import static net.justmili.libs.v1.event.server.ServerLifecycleEvents.*;
import static net.justmili.libs.v1.event.server.ServerTickEvents.*;

public final class ServerEventsBridge {
    private ServerEventsBridge() {}

    public static void init() {
        // Server Lifecycle
        ServerLifecycleEvents.SERVER_STARTING.register(server -> STARTING.invoker().onServerStarting(server));
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> STOPPING.invoker().onServerStopping(server));
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> STOPPED.invoker().onServerStopped(server));

        ServerLifecycleEvents.START_DATA_PACK_RELOAD.register((server, resourceManager) ->
                DATAPACK_RELOAD_PRE.invoker().onDatapackReloadPre(server, resourceManager));
        ServerLifecycleEvents.END_DATA_PACK_RELOAD.register((server, resourceManager, success) ->
                DATAPACK_RELOAD_POST.invoker().onDatapackReloadPost(server, resourceManager, success));
        ServerLifecycleEvents.SYNC_DATA_PACK_CONTENTS.register((player, joined) ->
            DATAPACK_SYNC_CONTENTS.invoker().onDatapackSyncContents(player, joined));

        // Connection
        // TODO: add

        // Server Ticks
        ServerTickEvents.START_SERVER_TICK.register(server -> {
            SERVER_PRE.invoker().onStartTick(server); // Tick server
            for (var player : server.getPlayerList().getPlayers()) PLAYER_PRE.invoker().onStartTick(player); // Tick players
        });
        ServerTickEvents.END_SERVER_TICK.register(server -> {
            SERVER_POST.invoker().onEndTick(server); // Tick server
            for (var player : server.getPlayerList().getPlayers()) PLAYER_POST.invoker().onEndTick(player); // Tick players
        });
        ServerTickEvents.START_WORLD_TICK.register(level -> {
            LEVEL_PRE.invoker().onStartTick(level); // Tick level
            for (var entity : level.getAllEntities()) ENTITY_PRE.invoker().onStartTick(entity); // Tick entities
        });
        ServerTickEvents.END_WORLD_TICK.register(level -> {
            LEVEL_POST.invoker().onEndTick(level); // Tick level
            for (var entity : level.getAllEntities()) ENTITY_POST.invoker().onEndTick(entity); // Tick entities
        });
    }
}