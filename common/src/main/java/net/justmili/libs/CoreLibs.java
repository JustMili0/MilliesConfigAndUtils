package net.justmili.libs;

import net.justmili.libs.v1.utils.TickUtil;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoreLibs {
    public static final String MODID = "milliescorelibs";
    public static final Logger LOGGER = LoggerFactory.getLogger("MilliesCoreLibs");

    public static void init() {
        TickUtil.registerProcessQueue();
        ExampleConfig.register();
    }

    public static ResourceLocation parse(String modId, String path) {return new ResourceLocation(modId, path);}
    public static ResourceLocation asResource(String path) {return new ResourceLocation(MODID, path);}
    public static ResourceLocation asMcResource(String path) {return new ResourceLocation("minecraft", path);}
    public static ResourceLocation asFabricResource(String path) {return new ResourceLocation("fabric", path);}
    public static ResourceLocation asForgeResource(String path) {return new ResourceLocation("forge", path);}
    public static ResourceLocation asNeoResource(String path) {return new ResourceLocation("neoforge", path);}
}