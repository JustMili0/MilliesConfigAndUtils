/*
 * This file contains code derived from Fabric API (https://github.com/FabricMC/fabric),
 * licensed under the Apache License, Version 2.0 (see LICENSE-APACHE-2).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Repackaged for Core Libraries; logic unchanged from the original.
 */

package net.justmili.libs.v1.event.client.lifecycle;

import net.justmili.libs.v1.event.base.Event;
import net.justmili.libs.v1.event.base.EventFactory;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.chunk.LevelChunk;

public final class ClientChunkEvents {
    private ClientChunkEvents() {
    }

    /**
     * Called when a chunk is loaded into a ClientWorld.
     *
     * <p>When this event is called, the chunk is already in the world.
     */
    public static final Event<Load> CHUNK_LOAD = EventFactory.createArrayBacked(ClientChunkEvents.Load.class, callbacks -> (clientWorld, chunk) -> {
        for (Load callback : callbacks) {
            callback.onChunkLoad(clientWorld, chunk);
        }
    });

    /**
     * Called when a chunk is about to be unloaded from a ClientWorld.
     *
     * <p>When this event is called, the chunk is still present in the world.
     */
    public static final Event<ClientChunkEvents.Unload> CHUNK_UNLOAD = EventFactory.createArrayBacked(ClientChunkEvents.Unload.class, callbacks -> (clientWorld, chunk) -> {
        for (Unload callback : callbacks) {
            callback.onChunkUnload(clientWorld, chunk);
        }
    });

    @FunctionalInterface
    public interface Load {
        void onChunkLoad(ClientLevel world, LevelChunk chunk);
    }

    @FunctionalInterface
    public interface Unload {
        void onChunkUnload(ClientLevel world, LevelChunk chunk);
    }
}
