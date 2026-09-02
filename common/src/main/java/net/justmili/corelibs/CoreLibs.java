package net.justmili.corelibs;

import net.justmili.corelibs.config.ExampleConfig;
import net.justmili.utils.utils.ModUtil;
import net.justmili.utils.utils.common.ResourceUtil;
import net.justmili.utils.utils.common.TickUtil;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreLibs {
    public static final String MODID = "corelibs";
    public static final String MODNAME = "Millie's Core Libraries";
    public static final Logger LOGGER = LoggerFactory.getLogger(CoreLibs.class);

    public static void init() {
        TickUtil.registerProcessQueue();

        ModUtil.markEndOfLife(MODNAME, MODID, false, false);
        ModUtil.markEndOfDevelopment(MODNAME, MODID, false, false);
        ExampleConfig.register();
    }

    public static ResourceLocation asId(String path) {
        return ResourceUtil.parse(MODID, path);
    }
}