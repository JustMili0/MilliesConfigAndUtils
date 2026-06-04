package net.justmili.libs.config;

import net.justmili.libs.v1.config.MConfigBuilder;
import net.justmili.libs.v1.config.entry.ConfigEntry;
import net.justmili.libs.v1.config.type.FileType;

public class Config {
    public static MConfigBuilder corelibs_client = new MConfigBuilder("corelibs", "client", FileType.JSON5, false);

    public static ConfigEntry<Boolean> alwaysPride;
    public static ConfigEntry<Boolean> alwaysDefault;

    public static void register() {
        var cfg = corelibs_client;

        cfg.openCat("Icon Type");
        alwaysPride = cfg.comment("Config Screen icons will always appear as their Pride Month variant")
            .define("always_pride_icons", false);
        alwaysDefault = cfg.comment("Config Screen icons will always appear as their Default variant")
            .define("always_default_icons", false);
        cfg.closeCat();

        cfg.build();
    }
}
