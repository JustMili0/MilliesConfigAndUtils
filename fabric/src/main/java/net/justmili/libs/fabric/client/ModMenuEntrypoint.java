package net.justmili.libs.fabric.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.justmili.libs.ExampleConfig;
import net.justmili.libs.client.screens.ConfigScreen;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(
            Component.translatable("config.title.millieslibs"),
            parent, List.of(ExampleConfig.server, ExampleConfig.client)
        );
    }
}