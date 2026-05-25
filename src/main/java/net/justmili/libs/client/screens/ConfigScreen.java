package net.justmili.libs.client.screens;

import net.justmili.libs.config.ConfigLoader;
import net.justmili.libs.config.build.ConfigEntry;
import net.justmili.libs.config.build.MConfigBuilder;
import net.justmili.libs.core.data.SharedValues;
import net.justmili.libs.core.util.TranslationKeyUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "NullableProblems"})
public class ConfigScreen extends Screen {
    private static final int
        // Placement and sizes
        PREVIEW_WIDTH = 160,
        ITEM_HEIGHT = 24,
        TAB_HEIGHT = 24,
        PANEL_BUTTON_HEIGHT = 20,
        PANEL_PADDING = 6;

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

    public ConfigScreen(Component title, Screen parent, MConfigBuilder builder) {
        this(title, parent, List.of(builder));
    }

    @Override
    protected void init() {
        List<Tab> tabs = builders.stream().map(builder -> (Tab) new ConfigTab(builder.getConfig(), this::setActiveConfig)).toList();

        tabBar = TabNavigationBar.builder(tabManager, this.width-PREVIEW_WIDTH).addTabs(tabs.toArray(new Tab[0])).build();
        addRenderableWidget(tabBar);
        tabBar.selectTab(0, false);

        int panelX = this.width-PREVIEW_WIDTH,
            panelBottom = this.height-PANEL_PADDING,
            twoButtonY = panelBottom-PANEL_BUTTON_HEIGHT * 2-4,
            twoButtonW = (PREVIEW_WIDTH-PANEL_PADDING * 3) / 2;

        addRenderableWidget(Button.builder(Component.literal("Done"), btn -> onClose())
            .bounds(panelX+PANEL_PADDING, panelBottom-PANEL_BUTTON_HEIGHT, PREVIEW_WIDTH-PANEL_PADDING * 2, PANEL_BUTTON_HEIGHT)
            .build());

        addRenderableWidget(Button.builder(Component.literal("Reset"), btn -> {
            if (hoveredEntry == null) return;

            Object previous = hoveredEntry.get();
            ((ConfigEntry<Object>) hoveredEntry).set(hoveredEntry.defaultValue());
            if (entryList != null) entryList.undoStack.push(() -> ((ConfigEntry<Object>) hoveredEntry).set(previous));
        }).bounds(panelX+PANEL_PADDING, twoButtonY, twoButtonW, PANEL_BUTTON_HEIGHT).build());

        addRenderableWidget(Button.builder(Component.literal("Undo"), btn -> {
            if (entryList != null && !entryList.undoStack.isEmpty()) entryList.undoStack.pop().run();
        }).bounds(panelX+PANEL_PADDING * 2+twoButtonW, twoButtonY, twoButtonW, PANEL_BUTTON_HEIGHT).build());

        if (!builders.isEmpty()) setActiveConfig(builders.getFirst().getConfig());
    }

    private void setActiveConfig(ConfigLoader config) {
        if (entryList != null) removeWidget(entryList);

        int listWidth = this.width-PREVIEW_WIDTH-2, listY = TAB_HEIGHT+2, listHeight = this.height-listY;
        entryList = new ConfigEntryList(this.minecraft, listWidth, listHeight, listY, ITEM_HEIGHT, config, entry -> hoveredEntry = entry);

        addRenderableWidget(entryList);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        if (tabBar != null)
            graphics.hLine(0, this.width-PREVIEW_WIDTH, tabBar.getRectangle().bottom(), SharedValues.COLOR_WHITE);

        int panelX = this.width-PREVIEW_WIDTH;
        graphics.fill(panelX, 0, panelX+1, this.height, SharedValues.COLOR_WHITE);

        renderPreviewPanel(graphics, panelX+PANEL_PADDING, PANEL_PADDING);
    }

    private void renderPreviewPanel(GuiGraphics graphics, int x, int y) {
        if (hoveredEntry == null) {
            graphics.drawString(font, Component.literal("Hover an entry"), x, y, SharedValues.COLOR_LIGHT_GRAY);
            return;
        }

        int textWidth = PREVIEW_WIDTH-PANEL_PADDING * 2;
        String modId = builders.stream()
            .filter(builder -> builder.getConfig().entries.containsKey(hoveredEntry.key())).findFirst()
            .map(builder -> builder.getConfig().modId).orElse("unknown"),

            nameKey = TranslationKeyUtil.varKey(modId, hoveredEntry.key()),
            descKey = TranslationKeyUtil.varDescKey(modId, hoveredEntry.key());

        // Name
        Component name = TranslationKeyUtil.resolve(nameKey).copy().withStyle(style -> style.withBold(true));
        graphics.drawWordWrap(font, name, x, y, textWidth, SharedValues.COLOR_WHITE);
        y += font.wordWrapHeight(name, textWidth)+4;

        // Desc
        Component desc = TranslationKeyUtil.resolve(descKey);
        graphics.drawWordWrap(font, desc, x, y, textWidth, SharedValues.COLOR_LIGHT_GRAY);
    }

    @Override
    public void onClose() {
        this.minecraft.setScreen(parent);
    }

    private record ConfigTab(ConfigLoader config, Consumer<ConfigLoader> onSelect) implements Tab {
        @Override
        public Component getTabTitle() {
            return Component.literal(config.name != null && !config.name.isBlank() ? config.name : config.modId);
        }

        @Override
        public Component getTabExtraNarration() {
            return null;
        }

        @Override
        public void visitChildren(Consumer<AbstractWidget> consumer) {
            onSelect.accept(config);
        }

        @Override
        public void doLayout(ScreenRectangle rectangle) {
        }
    }
}