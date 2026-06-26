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
import net.minecraft.world.entity.Entity;

public final class ClientEntityEvents {
    private ClientEntityEvents() {
    }

    /**
     * Called when an Entity is loaded into a ClientWorld.
     *
     * <p>When this event is called, the chunk is already in the world.
     */
    public static final Event<Load> ENTITY_LOAD = EventFactory.createArrayBacked(ClientEntityEvents.Load.class, callbacks -> (entity, world) -> {
        for (Load callback : callbacks) {
            callback.onLoad(entity, world);
        }
    });

    /**
     * Called when an Entity is about to be unloaded from a ClientWorld.
     *
     * <p>This event is called before the entity is unloaded from the world.
     */
    public static final Event<ClientEntityEvents.Unload> ENTITY_UNLOAD = EventFactory.createArrayBacked(ClientEntityEvents.Unload.class, callbacks -> (entity, world) -> {
        for (Unload callback : callbacks) {
            callback.onUnload(entity, world);
        }
    });

    @FunctionalInterface
    public interface Load {
        void onLoad(Entity entity, ClientLevel world);
    }

    @FunctionalInterface
    public interface Unload {
        void onUnload(Entity entity, ClientLevel world);
    }
}
