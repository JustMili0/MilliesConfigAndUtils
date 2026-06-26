/*
 * This file contains code derived from Fabric API (https://github.com/FabricMC/fabric),
 * licensed under the Apache License, Version 2.0 (see LICENSE-APACHE-2).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Modified by JustMili for Core Libraries: Renamed constants
 * START_DATA_PACK_RELOAD -> DATAPACK_RELOAD_PRE
 * END_DATA_PACK_RELOAD -> DATAPACK_RELOAD_POST
 * SYNC_DATA_PACK_CONTENTS -> DATAPACK_SYNC_CONTENTS
 */

package net.justmili.libs.v1.event.server.lifecycle;

import net.justmili.libs.v1.event.base.Event;
import net.justmili.libs.v1.event.base.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.players.PlayerList;

public final class ServerLifecycleEvents {
    private ServerLifecycleEvents() {
    }

    /**
     * Called when a Minecraft server is starting.
     *
     * <p>This occurs before the {@link PlayerList player manager} and any worlds are loaded.
     */
    public static final Event<ServerStarting> SERVER_STARTING = EventFactory.createArrayBacked(ServerStarting.class, callbacks -> server -> {
        for (ServerStarting callback : callbacks) {
            callback.onServerStarting(server);
        }
    });

    /**
     * Called when a Minecraft server has started and is about to tick for the first time.
     *
     * <p>At this stage, all worlds are live.
     */
    public static final Event<ServerStarted> SERVER_STARTED = EventFactory.createArrayBacked(ServerStarted.class, (callbacks) -> (server) -> {
        for (ServerStarted callback : callbacks) {
            callback.onServerStarted(server);
        }
    });

    /**
     * Called when a Minecraft server has started shutting down.
     * This occurs before the server's network channel is closed and before any players are disconnected.
     *
     * <p>For example, an integrated server will begin stopping, but its client may continue to run.
     *
     * <p>All worlds are still present and can be modified.
     */
    public static final Event<ServerStopping> SERVER_STOPPING = EventFactory.createArrayBacked(ServerStopping.class, (callbacks) -> (server) -> {
        for (ServerStopping callback : callbacks) {
            callback.onServerStopping(server);
        }
    });

    /**
     * Called when a Minecraft server has stopped.
     * All worlds have been closed and all (block)entities and players have been unloaded.
     *
     * <p>For example, an {@link net.fabricmc.api.EnvType#CLIENT integrated server} will begin stopping, but its client may continue to run.
     * Meanwhile, for a {@link net.fabricmc.api.EnvType#SERVER dedicated server}, this will be the last event called.
     */
    public static final Event<ServerStopped> SERVER_STOPPED = EventFactory.createArrayBacked(ServerStopped.class, callbacks -> server -> {
        for (ServerStopped callback : callbacks) {
            callback.onServerStopped(server);
        }
    });

    /**
     * Called when a Minecraft server is about to send tag and recipe data to a player.
     * @see SyncDataPackContents
     */
    public static final Event<SyncDataPackContents> DATAPACK_SYNC_CONTENTS = EventFactory.createArrayBacked(SyncDataPackContents.class, callbacks -> (player, joined) -> {
        for (SyncDataPackContents callback : callbacks) {
            callback.onSyncDataPackContents(player, joined);
        }
    });

    /**
     * Called before a Minecraft server reloads data packs.
     */
    public static final Event<StartDataPackReload> DATAPACK_RELOAD_PRE = EventFactory.createArrayBacked(StartDataPackReload.class, callbacks -> (server, serverResourceManager) -> {
        for (StartDataPackReload callback : callbacks) {
            callback.startDataPackReload(server, serverResourceManager);
        }
    });

    /**
     * Called after a Minecraft server has reloaded data packs.
     *
     * <p>If reloading data packs was unsuccessful, the current data packs will be kept.
     */
    public static final Event<EndDataPackReload> DATAPACK_RELOAD_POST = EventFactory.createArrayBacked(EndDataPackReload.class, callbacks -> (server, serverResourceManager, success) -> {
        for (EndDataPackReload callback : callbacks) {
            callback.endDataPackReload(server, serverResourceManager, success);
        }
    });

    @FunctionalInterface
    public interface ServerStarting {
        void onServerStarting(MinecraftServer server);
    }

    @FunctionalInterface
    public interface ServerStarted {
        void onServerStarted(MinecraftServer server);
    }

    @FunctionalInterface
    public interface ServerStopping {
        void onServerStopping(MinecraftServer server);
    }

    @FunctionalInterface
    public interface ServerStopped {
        void onServerStopped(MinecraftServer server);
    }

    @FunctionalInterface
    public interface SyncDataPackContents {
        /**
         * Called right before tags and recipes are sent to a player,
         * either because the player joined, or because the server reloaded resources.
         * The {@linkplain MinecraftServer#getResourceManager() server resource manager} is up-to-date when this is called.
         *
         * <p>For example, this event can be used to sync data loaded with custom resource reloaders.
         *
         * @param player Player to which the data is being sent.
         * @param joined True if the player is joining the server, false if the server finished a successful resource reload.
         */
        void onSyncDataPackContents(ServerPlayer player, boolean joined);
    }

    @FunctionalInterface
    public interface StartDataPackReload {
        void startDataPackReload(MinecraftServer server, CloseableResourceManager resourceManager);
    }

    @FunctionalInterface
    public interface EndDataPackReload {
        /**
         * Called after data packs on a Minecraft server have been reloaded.
         *
         * <p>If the reload was not successful, the old data packs will be kept.
         *
         * @param server the server
         * @param resourceManager the resource manager
         * @param success if the reload was successful
         */
        void endDataPackReload(MinecraftServer server, CloseableResourceManager resourceManager, boolean success);
    }
}
