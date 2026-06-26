/*
 * This file contains code derived from Fabric API (https://github.com/FabricMC/fabric),
 * licensed under the Apache License, Version 2.0 (see LICENSE-APACHE-2).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Repackaged for Core Libraries; logic unchanged from the original.
 */

package net.justmili.libs.v1.event.base;

import java.lang.reflect.Array;
import java.util.Arrays;

import net.justmili.libs.v1.base.SortableNode;
import net.minecraft.resources.ResourceLocation;

/**
 * Data of an {@link ArrayBackedEvent} phase.
 */
public class EventPhaseData<T> extends SortableNode<EventPhaseData<T>> {
    public final ResourceLocation id;
    T[] listeners;

    @SuppressWarnings("unchecked")
    EventPhaseData(ResourceLocation id, Class<?> listenerClass) {
        this.id = id;
        this.listeners = (T[]) Array.newInstance(listenerClass, 0);
    }

    void addListener(T listener) {
        int oldLength = listeners.length;
        listeners = Arrays.copyOf(listeners, oldLength + 1);
        listeners[oldLength] = listener;
    }

    @Override
    protected String getDescription() {
        return id.toString();
    }
}
