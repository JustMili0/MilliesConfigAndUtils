package net.justmili.libs.config;

import net.justmili.libs.v1.config.MConfigBuilder;
import net.justmili.libs.v1.config.type.FileType;

public class Config {
    public static MConfigBuilder corelibs_client = new MConfigBuilder("corelibs", "client", FileType.JSON5, true);

    public static void register() {
        // TODO: make a config to customize the config screen
        // *primarily the look of the icons and stuff
        var cfg = corelibs_client;

        cfg.openCat("Config Screen Customization");

        cfg.openCat("Icon Type");
        // icons, enum selector type
        cfg.closeCat();

        cfg.openCat("Widget Type");
        // widgets, enum selector type
        // Allows configuring does the player want e.g. bools are true/false vanilla buttons or a custom widget
        // Same with text/number inputs
        cfg.closeCat();

        cfg.closeCat();

        cfg.build();
    }
}
