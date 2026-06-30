package net.justmili.libs;

import net.justmili.libs.config.Config;
import net.justmili.libs.config.ExampleConfig;
import net.justmili.libs.v1.utils.ResourceUtil;
import net.justmili.libs.v1.utils.TickUtil;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreLibs {
    public static final String MODID = "corelibs";
    public static final Logger LOGGER = LoggerFactory.getLogger(CoreLibs.class);

    public static void init() {
        Config.register();

        TickUtil.registerProcessQueue();
        ExampleConfig.register();
    }

    public static ResourceLocation asResource(String path) {
        return ResourceUtil.parse(MODID, path);
    }
}