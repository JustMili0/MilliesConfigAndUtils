package net.justmili.libs;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigLib implements ModInitializer {
    public static final String MODID = "config_n_utils";
    public static final Logger LOGGER = LoggerFactory.getLogger("Millie's Config & Utils");

    @Override
    public void onInitialize() {
        ExampleConfig.register();
    }
}