package net.justmili.libs;

import net.justmili.libs.config.ExampleConfig;
import net.justmili.libs.v1.utils.ModUtil;
import net.justmili.libs.v1.utils.common.ResourceUtil;
import net.justmili.libs.v1.utils.common.TickUtil;
import net.justmili.libs.v1.utils.server.ServerUtil;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreLibs {
    public static final String MODID = "corelibs";
    public static final String MODNAME = "Millie's Core Libraries";
    public static final Logger LOGGER = LoggerFactory.getLogger(CoreLibs.class);

    public static void init() {
        ServerUtil.setServer();
        TickUtil.registerProcessQueue();

        ModUtil.markEndOfLife(MODNAME, MODID, false, false);
        ModUtil.markEndOfDevelopment(MODNAME, MODID, false, false);
        ExampleConfig.register();
    }

    public static ResourceLocation asResource(String path) {
        return ResourceUtil.parse(MODID, path);
    }
}