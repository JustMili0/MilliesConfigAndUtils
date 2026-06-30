/*
 * This file contains code derived from Fabric API (https://github.com/FabricMC/fabric),
 * licensed under the Apache License, Version 2.0 (see LICENSE-APACHE-2).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Modified by JustMili for Core Libraries:
 * Renamed constants BLOCK_ENTITY_LOAD -> ENTITY_LOAD, BLOCK_ENTITY_UNLOAD -> ENTITY_ULOAD;
 * logic remains unchanged.
 */

package net.justmili.libs.v1.event.client.lifecycle;

import net.justmili.libs.v1.event.base.Event;
import net.justmili.libs.v1.event.base.EventFactory;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class ClientBlockEntityEvents {
    private ClientBlockEntityEvents() {
    }

    /**
     * Called when a BlockEntity is loaded into a ClientWorld.
     *
     * <p>When this event is called, the block entity is already in the world.
     * However, its data might not be loaded yet, so don't rely on it.
     */
    public static final Event<Load> ENTITY_LOAD = EventFactory.createArrayBacked(ClientBlockEntityEvents.Load.class, callbacks -> (blockEntity, world) -> {
        for (Load callback : callbacks) {
            callback.onLoad(blockEntity, world);
        }
    });

    /**
     * Called when a BlockEntity is about to be unloaded from a ClientWorld.
     *
     * <p>When this event is called, the block entity is still present on the world.
     */
    public static final Event<ClientBlockEntityEvents.Unload> ENTITY_UNLOAD = EventFactory.createArrayBacked(ClientBlockEntityEvents.Unload.class, callbacks -> (blockEntity, world) -> {
        for (Unload callback : callbacks) {
            callback.onUnload(blockEntity, world);
        }
    });

    @FunctionalInterface
    public interface Load {
        void onLoad(BlockEntity blockEntity, ClientLevel world);
    }

    @FunctionalInterface
    public interface Unload {
        void onUnload(BlockEntity blockEntity, ClientLevel world);
    }
}
