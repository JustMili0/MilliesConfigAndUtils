/*
 * This file contains code derived from Fabric API (https://github.com/FabricMC/fabric),
 * licensed under the Apache License, Version 2.0 (see LICENSE-APACHE-2).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Modified by JustMili for Core Libraries: Renamed constants
 * START_SERVER_TICK -> SERVER_PRE, END_SERVER_TICK -> SERVER_POST
 * START_WORLD_TICK -> WORLD_PRE, END_WORLD_TICK -> WORLD_POST;
 * logic unchanged.
 */

package net.justmili.libs.v1.event.server.lifecycle;

import net.justmili.libs.v1.event.base.Event;
import net.justmili.libs.v1.event.base.EventFactory;
import net.minecraft.server.MinecraftServer;

import net.minecraft.server.level.ServerLevel;

public final class ServerTickEvents {
    private ServerTickEvents() {
    }

    /**
     * Called at the start of the server tick.
     */
    public static final Event<StartTick> SERVER_PRE = EventFactory.createArrayBacked(StartTick.class, callbacks -> server -> {
        for (StartTick event : callbacks) {
            event.onStartTick(server);
        }
    });

    /**
     * Called at the end of the server tick.
     */
    public static final Event<EndTick> SERVER_POST = EventFactory.createArrayBacked(EndTick.class, callbacks -> server -> {
        for (EndTick event : callbacks) {
            event.onEndTick(server);
        }
    });

    /**
     * Called at the start of a ServerLevel's tick.
     */
    public static final Event<StartWorldTick> WORLD_PRE = EventFactory.createArrayBacked(StartWorldTick.class, callbacks -> world -> {
        for (StartWorldTick callback : callbacks) {
            callback.onStartTick(world);
        }
    });

    /**
     * Called at the end of a ServerLevel's tick.
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
        void onStartTick(MinecraftServer server);
    }

    @FunctionalInterface
    public interface EndTick {
        void onEndTick(MinecraftServer server);
    }

    @FunctionalInterface
    public interface StartWorldTick {
        void onStartTick(ServerLevel world);
    }

    @FunctionalInterface
    public interface EndWorldTick {
        void onEndTick(ServerLevel world);
    }
}