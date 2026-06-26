/*
 * This file contains code derived from Fabric API (https://github.com/FabricMC/fabric),
 * licensed under the Apache License, Version 2.0 (see LICENSE-APACHE-2).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Modified by JustMili for Core Libraries: Renamed constants
 * EVENT -> RENDER
 * logic remains unchanged.
 */

package net.justmili.libs.v1.event.client.render;

import net.justmili.libs.v1.event.base.Event;
import net.justmili.libs.v1.event.base.EventFactory;
import org.jetbrains.annotations.Nullable;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;

/**
 * Allows registering a mapping from {@link TooltipComponent} to {@link ClientTooltipComponent}.
 * This allows custom tooltips for items: first, override {@link Item#getTooltipImage} and return a custom {@code TooltipData}.
 * Second, register a listener to this event and convert the data to your component implementation if it's an instance of your data class.
 *
 * <p>Note that failure to map some data to a component will throw an exception,
 * so make sure that any data you return in {@link Item#getTooltipImage} will be handled by one of the callbacks.
 */
public interface TooltipRenderEvent {
    Event<TooltipRenderEvent> RENDER = EventFactory.createArrayBacked(TooltipRenderEvent.class, listeners -> data -> {
        for (TooltipRenderEvent listener : listeners) {
            ClientTooltipComponent component = listener.getComponent(data);

            if (component != null) {
                return component;
            }
        }

        return null;
    });

    /**
     * Return the tooltip component for the passed data, or null if none is available.
     */
    @Nullable
    ClientTooltipComponent getComponent(TooltipComponent data);
}
