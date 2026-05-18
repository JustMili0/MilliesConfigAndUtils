package net.justmili.libs;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Library implements ModInitializer {
    public static final String MODID = "millieslibs";
    public static final Logger LOGGER = LoggerFactory.getLogger("Millie's Config & Utils");

    @Override
    public void onInitialize() {
        ExampleConfig.register();
    }
}