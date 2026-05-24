package net.justmili.libs.client;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.justmili.libs.ExampleConfig;
import net.justmili.libs.client.screens.ConfigScreen;
import net.minecraft.network.chat.Component;

public class ModMenuEntrypoint implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new ConfigScreen(
            Component.literal("Millie's Config & Utils"),
            parent, ExampleConfig.builder
        );
    }
}