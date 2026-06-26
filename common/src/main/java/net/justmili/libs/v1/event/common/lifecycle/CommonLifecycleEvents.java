/*
 * This file contains code derived from Fabric API (https://github.com/FabricMC/fabric),
 * licensed under the Apache License, Version 2.0 (see LICENSE-APACHE-2).
 *
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Repackaged for Core Libraries; logic unchanged from the original.
 */

package net.justmili.libs.v1.event.common.lifecycle;

import net.justmili.libs.v1.event.base.Event;
import net.justmili.libs.v1.event.base.EventFactory;
import net.minecraft.core.RegistryAccess;

public class CommonLifecycleEvents {
    private CommonLifecycleEvents() {
    }

    /**
     * Called when tags are loaded or updated.
     */
    public static final Event<TagsLoaded> TAGS_LOADED = EventFactory.createArrayBacked(TagsLoaded.class, callbacks -> (registries, client) -> {
        for (TagsLoaded callback : callbacks) {
            callback.onTagsLoaded(registries, client);
        }
    });

    public interface TagsLoaded {
        /**
         * @param registries Up-to-date registries from which the tags can be retrieved.
         * @param client True if the client just received a sync packet, false if the server just (re)loaded the tags.
         */
        void onTagsLoaded(RegistryAccess registries, boolean client);
    }
}
