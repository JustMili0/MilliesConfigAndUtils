package net.justmili.util.utils;

import net.justmili.api.events.server.ServerLevelEvents;
import net.justmili.api.events.server.ServerLifecycleEvents;
import net.justmili.corelibs.CoreLibs;

public class ModUtil {
    // TODO: Add Javadoc

    public static void markEndOfLife(String modName, String modId, boolean isDeadVersion, boolean shouldNotifyChat) {
        if (!isDeadVersion) return;
        ServerLifecycleEvents.STARTING.register(server -> {
            CoreLibs.LOGGER.warn("""
                \nYou are using the last version of "{}" (ID: {}) that supports Minecraft {}!
                This means that mod is no longer developed for this Minecraft version.
                It is recommended you update to a newer Minecraft version for a more up-to-date experience.
              """, modName, modId, server.getServerVersion());
        });
        ServerLevelEvents.LEVEL_LOAD.register((server, level) -> {
            // TODO: Change to chat msg per-player and make it get sent only once ever
            // TODO: Change to when client loads the world, otherwise it'll appear twice on servers
            if (!shouldNotifyChat) return;
            CoreLibs.LOGGER.warn("""
                You are using the last version of "{}" that supports Minecraft {}!
                This means that mod is no longer developed for this Minecraft version.
                It is recommended you update to a newer Minecraft version for a more up-to-date experience.
                \n
                This message will not appear in chat again.
              """, modName, server.getServerVersion());
        });
    }
    public static void markEndOfDevelopment(String modName, String modId, boolean isDeadMod, boolean shouldNotifyChat) {
        if (!isDeadMod) return;
        ServerLifecycleEvents.STARTING.register(server -> {
            // TODO: Expand on message
            CoreLibs.LOGGER.warn("""
                Mod "{}" is no longer in development!
              """, modName);
        });
        ServerLevelEvents.LEVEL_LOAD.register((server, level) -> {
            // TODO: Change to chat msg per-player and make it get sent only once ever
            // TODO: Change to when client loads the world, otherwise it'll appear twice on servers
            // TODO: Expand on message
            if (!shouldNotifyChat) return;
            CoreLibs.LOGGER.warn("""
                Mod "{}" is no longer in development!
                \n
                This message will not appear in chat again.
              """, modName);
        });
    }
}
