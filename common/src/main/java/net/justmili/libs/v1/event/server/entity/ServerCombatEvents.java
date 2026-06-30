/*
 * This file contains code derived from Fabric API (https://github.com/FabricMC/fabric),
 * licensed under the Apache License, Version 2.0 (see LICENSE-APACHE-2).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Modified by JustMili for Core Libraries:
 * Renamed constants AFTER_KILLED_OTHER_ENTITY -> KILLED_OTHER_ENTITY_POST;
 * Renamed class ServerEntityCombatEvents -> ServerCombatEvents
 * logic remains unchanged.
 */

package net.justmili.libs.v1.event.server.entity;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * Events related to entities in combat.
 */
public final class ServerCombatEvents {
    /**
     * An event that is called after an entity is directly responsible for killing another entity.
     *
     * @see Entity#killedEntity(ServerLevel, LivingEntity)
     */
    public static final Event<AfterKilledOtherEntity> KILLED_OTHER_ENTITY_POST = EventFactory.createArrayBacked(AfterKilledOtherEntity.class, callbacks -> (level, entity, killedEntity) -> {
        for (AfterKilledOtherEntity callback : callbacks) {
            callback.afterKilledOtherEntity(level, entity, killedEntity);
        }
    });

    @FunctionalInterface
    public interface AfterKilledOtherEntity {
        /**
         * Called after an entity has killed another entity.
         *
         * @param level the level
         * @param entity the entity
         * @param killedEntity the entity which was killed by the {@code entity}
         */
        void afterKilledOtherEntity(ServerLevel level, Entity entity, LivingEntity killedEntity);
    }

    private ServerCombatEvents() {
    }
}