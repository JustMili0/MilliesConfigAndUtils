package net.justmili.config;

import net.justmili.config.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLib {
    public static final String MODID = "milliesconfiglib";
    public static final Logger LOGGER = LoggerFactory.getLogger("Millie's Config Lib");

    public static void init() {
        // Common
        Config.register();
    }
}
