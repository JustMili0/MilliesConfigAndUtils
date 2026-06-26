/*
 * This file contains code derived from Fabric API (https://github.com/FabricMC/fabric),
 * licensed under the Apache License, Version 2.0 (see LICENSE-APACHE-2).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Modified by JustMili for Core Libraries: Renamed constants
 * START_CLIENT_TICK -> CLIENT_PRE, END_CLIENT_TICK -> CLIENT_POST,
 * START_WORLD_TICK -> WORLD_PRE, END_WORLD_TICK -> WORLD_POST;
 * logic unchanged.
 */

package net.justmili.libs.v1.event.client.lifecycle;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;

public final class ClientTickEvents {
    private ClientTickEvents() {
    }

    /**
     * Called at the start of the client tick.
     */
    public static final Event<StartTick> CLIENT_PRE = EventFactory.createArrayBacked(StartTick.class, callbacks -> client -> {
        for (StartTick event : callbacks) {
            event.onStartTick(client);
        }
    });

    /**
     * Called at the end of the client tick.
     */
    public static final Event<EndTick> CLIENT_POST = EventFactory.createArrayBacked(EndTick.class, callbacks -> client -> {
        for (EndTick event : callbacks) {
            event.onEndTick(client);
        }
    });

    /**
     * Called at the start of a ClientWorld's tick.
     */
    public static final Event<StartWorldTick> WORLD_PRE = EventFactory.createArrayBacked(StartWorldTick.class, callbacks -> world -> {
        for (StartWorldTick callback : callbacks) {
            callback.onStartTick(world);
        }
    });

    /**
     * Called at the end of a ClientWorld's tick.
     *
     * <p>End of world tick may be used to start async computations for the next tick.
     */
    public static final Event<EndWorldTick> WORLD_POST = EventFactory.createArrayBacked(EndWorldTick.class, callbacks -> world -> {
        for (EndWorldTick callback : callbacks) {
            callback.onEndTick(world);
        }
    });

    @FunctionalInterface
    public interface StartTick {
        void onStartTick(Minecraft client);
    }

    @FunctionalInterface
    public interface EndTick {
        void onEndTick(Minecraft client);
    }

    @FunctionalInterface
    public interface StartWorldTick {
        void onStartTick(ClientLevel world);
    }

    @FunctionalInterface
    public interface EndWorldTick {
        void onEndTick(ClientLevel world);
    }
}
