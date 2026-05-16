package net.justmili.config.config;

import net.justmili.config.ConfigLib;
import net.justmili.config.create.MConfigBuilder;
import net.justmili.config.create.ConfigEntry;
import net.justmili.config.data.FileType;

public class Config {
    public static ConfigEntry<Integer> someInt;

    public static void register() {
        MConfigBuilder builder = new MConfigBuilder(ConfigLib.MODID, "example-config", FileType.PROPERTIES, true);

        someInt = builder.comment("a comment").define("someInt", 1, 0, 5);

        builder.build();
    }
}
