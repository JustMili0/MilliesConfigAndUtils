package net.justmili.libs.client.screens;

import net.justmili.libs.config.ConfigLoader;
import net.justmili.libs.config.build.ConfigEntry;
import net.justmili.libs.config.build.MConfigBuilder;
import net.justmili.libs.utils.TranslationKeyUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfigScreen extends Screen {
    private static final int PREVIEW_PANEL_WIDTH = 200;
    private static final int LIST_ITEM_HEIGHT = 24;
    private static final int TAB_HEIGHT = 24;
    private static final int BOTTOM_BUTTONS_HEIGHT = 36;

    public final Screen parent;
    private final List<MConfigBuilder> builders;
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private TabNavigationBar tabBar;
    private ConfigEntryList entryList;
    private ConfigEntry<?> hoveredEntry = null;

    public ConfigScreen(Component title, Screen parent, List<MConfigBuilder> builders) {
        super(title);
        this.parent = parent;
        this.builders = builders;
    }

    // Convenience constructor for single builder
    public ConfigScreen(Component title, Screen parent, MConfigBuilder builder) {
        this(title, parent, List.of(builder));
    }

    @Override
    protected void init() {
        List<Tab> tabs = new ArrayList<>();
        for (MConfigBuilder builder : builders) {
            ConfigLoader config = builder.getConfig();
            tabs.add(new ConfigTab(config, this::setActiveConfig));
        }

        tabBar = TabNavigationBar.builder(tabManager, this.width)
            .addTabs(tabs.toArray(new Tab[0]))
            .build();
        addRenderableWidget(tabBar);
        tabBar.selectTab(0, false);

        // Done button
        addRenderableWidget(Button.builder(Component.literal("Done"), btn -> onClose())
            .bounds(this.width/2 - 75, this.height - BOTTOM_BUTTONS_HEIGHT + 8, 150, 20)
            .build());

        // Init entry list with first builder
        if (!builders.isEmpty()) setActiveConfig(builders.getFirst().getConfig());
    }

    private void setActiveConfig(ConfigLoader config) {
        if (entryList != null) removeWidget(entryList);

        int listWidth = this.width - PREVIEW_PANEL_WIDTH - 8;
        int listY = TAB_HEIGHT + 2;
        int listHeight = this.height - listY - BOTTOM_BUTTONS_HEIGHT;

        entryList = new ConfigEntryList(this.minecraft, listWidth, listHeight, listY, LIST_ITEM_HEIGHT, config, entry -> hoveredEntry = entry);
        addRenderableWidget(entryList);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        // Separator line below tab bar
        if (tabBar != null)
            graphics.hLine(0, this.width, tabBar.getRectangle().bottom(), 0xFFFFFFFF);

        // Preview panel background
        int panelX = this.width - PREVIEW_PANEL_WIDTH;
        int panelY = TAB_HEIGHT + 2;
        int panelHeight = this.height - panelY - BOTTOM_BUTTONS_HEIGHT;
        graphics.fill(panelX, panelY, this.width, panelY + panelHeight, 0xAA000000);

        renderPreviewPanel(graphics, panelX + 8, panelY + 8);
    }

    private void renderPreviewPanel(GuiGraphics graphics, int x, int y) {
        if (hoveredEntry == null) {
            graphics.drawString(font, Component.literal("Hover an entry"), x, y, 0xAAAAAA);
            return;
        }

        String modId = builders.stream()
            .filter(b -> b.getConfig().entries.containsKey(hoveredEntry.key()))
            .findFirst()
            .map(b -> b.getConfig().modId)
            .orElse("unknown");

        // Entry name
        Component name = Component.translatableWithFallback(TranslationKeyUtil.varKey(modId, hoveredEntry.key()), hoveredEntry.key());
        graphics.drawString(font, name, x, y, 0xFFFFFF);

        // Entry description
        Component desc = Component.translatable(TranslationKeyUtil.varDescKey(modId, hoveredEntry.key()));
        int descY = y + font.lineHeight + 4;
        for (FormattedCharSequence line : font.split(desc, PREVIEW_PANEL_WIDTH - 16)) {
            graphics.drawString(font, line, x, descY, 0xAAAAAA);
            descY += font.lineHeight + 2;
        }

        // Current value
        int valueY = descY + 8;
        graphics.drawString(font, Component.literal("Value: "+hoveredEntry.get()), x, valueY, 0xFFFF55);

        // Default value
        graphics.drawString(font, Component.literal("Default: "+hoveredEntry.defaultValue()), x, valueY + font.lineHeight + 2, 0xAAAAAA);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    // Tab implementation
    private class ConfigTab implements Tab {
        private final ConfigLoader config;
        private final Consumer<ConfigLoader> onSelect;

        public ConfigTab(ConfigLoader config, Consumer<ConfigLoader> onSelect) {
            this.config = config;
            this.onSelect = onSelect;
        }

        @Override
        public Component getTabTitle() {
            return Component.literal(config.modId + (config.name != null && !config.name.isBlank() ? " - " + config.name : ""));
        }

        @Override
        public Component getTabExtraNarration() {
            return null;
        }

        @Override
        public void visitChildren(Consumer<net.minecraft.client.gui.components.AbstractWidget> consumer) {
            onSelect.accept(config);
        }

        @Override
        public void doLayout(ScreenRectangle rectangle) {

        }
    }
}