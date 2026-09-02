package net.justmili.corelibs.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.justmili.configscreen.ConfigScreen;
import net.justmili.corelibs.config.ExampleConfig;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(
            Component.translatable("config.title.corelibs"),
            parent, List.of(ExampleConfig.server, ExampleConfig.client)
            //parent, Config.corelibs_client
        );
    }
}