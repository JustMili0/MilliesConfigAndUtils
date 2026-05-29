package net.justmili.libs.v1.config.screen;

import net.justmili.libs.v1.config.entry.ConfigEntry;
import net.justmili.libs.v1.config.MConfigBuilder;
import net.justmili.libs.v1.config.ConfigLoader;
import net.justmili.libs.v1.config.items.CategoryItem;
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
        PREVIEW_WIDTH = 160,
        ITEM_HEIGHT = 24,
        TAB_HEIGHT = 24,
        PANEL_BUTTON_HEIGHT = 20,
        PANEL_PADDING = 6;

    public final Screen parent;
    private final List<MConfigBuilder> builders;
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private TabNavigationBar tabBar;
    private ConfigScreenBuilder entryList;
    private ConfigEntry<?> hoveredEntry = null;
    private CategoryItem hoveredCategory = null;

    public ConfigScreen(Component title, Screen parent, List<MConfigBuilder> builders) {
        super(title);
        this.parent = parent;
        this.builders = builders;
    }

    public ConfigScreen(Component title, Screen parent, MConfigBuilder builder) {
        this(title, parent, List.of(builder));
    }

    private void setActiveConfig(ConfigLoader config) {
        if (entryList != null) removeWidget(entryList);

        int listWidth = this.width-PREVIEW_WIDTH-2, listY = TAB_HEIGHT+2, listHeight = this.height-listY;
        entryList = new ConfigScreenBuilder(this.minecraft, listWidth, listHeight, listY, ITEM_HEIGHT, config,
            entry -> hoveredEntry = entry, category -> {
            hoveredCategory = category;
            hoveredEntry = null;
        });

        addRenderableWidget(entryList);
    }

    private void renderPreviewPanel(GuiGraphics graphics, int x, int y) {
        int textWidth = PREVIEW_WIDTH-PANEL_PADDING * 2;

        if (hoveredEntry != null) {
            String modId = builders.stream()
                .filter(builder -> builder.getConfig().entries.containsKey(hoveredEntry.key())).findFirst()
                .map(builder -> builder.getConfig().modId).orElse("unknown");

            Component name = ScreenElements.resolve(ScreenElements.varKey(modId, hoveredEntry.key())).copy().withStyle(style -> style.withBold(true));
            graphics.drawWordWrap(font, name, x, y, textWidth, ScreenElements.COLOR_WHITE);
            y += font.wordWrapHeight(name, textWidth)+4;

            graphics.drawWordWrap(font, ScreenElements.resolve(
                ScreenElements.varDescKey(modId, hoveredEntry.key())), x, y, textWidth, ScreenElements.COLOR_WHITE);

        } else if (hoveredCategory != null) {
            String modId = builders.stream()
                .filter(builder -> builder.getConfig().modId != null).findFirst()
                .map(builder -> builder.getConfig().modId).orElse("unknown");

            Component name = ScreenElements.resolve(ScreenElements.catKey(modId, hoveredCategory.name())).copy().withStyle(style -> style.withBold(true));
            graphics.drawWordWrap(font, name, x, y, textWidth, ScreenElements.COLOR_WHITE);
            y += font.wordWrapHeight(name, textWidth)+4;

            graphics.drawWordWrap(font, ScreenElements.resolve(
                ScreenElements.catDescKey(modId, hoveredCategory.name())), x, y, textWidth, ScreenElements.COLOR_WHITE);
        }
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

        addRenderableWidget(Button.builder(Component.translatable("gui.config.done"), button -> onClose())
            .bounds(panelX+PANEL_PADDING, panelBottom-PANEL_BUTTON_HEIGHT, PREVIEW_WIDTH-PANEL_PADDING * 2, PANEL_BUTTON_HEIGHT)
            .build());

        addRenderableWidget(Button.builder(Component.translatable("gui.config.reset"), button -> {
            if (hoveredEntry == null) return;

            Object previous = hoveredEntry.get();
            ((ConfigEntry<Object>) hoveredEntry).set(hoveredEntry.defaultValue());
            if (entryList != null) entryList.undoStack.push(() -> ((ConfigEntry<Object>) hoveredEntry).set(previous));
        }).bounds(panelX+PANEL_PADDING, twoButtonY, twoButtonW, PANEL_BUTTON_HEIGHT).build());

        addRenderableWidget(Button.builder(Component.translatable("gui.config.undo"), button -> {
            if (entryList != null && !entryList.undoStack.isEmpty()) entryList.undoStack.pop().run();
        }).bounds(panelX+PANEL_PADDING * 2+twoButtonW, twoButtonY, twoButtonW, PANEL_BUTTON_HEIGHT).build());

        if (!builders.isEmpty()) setActiveConfig(builders.get(0).getConfig());
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);

        if (tabBar != null)
            graphics.hLine(0, this.width-PREVIEW_WIDTH, tabBar.getRectangle().bottom(), ScreenElements.COLOR_WHITE);

        int panelX = this.width-PREVIEW_WIDTH;
        graphics.fill(panelX, 0, panelX+1, this.height, ScreenElements.COLOR_WHITE);

        renderPreviewPanel(graphics, panelX+PANEL_PADDING, PANEL_PADDING);
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
        public void visitChildren(Consumer<AbstractWidget> consumer) {
            onSelect.accept(config);
        }

        @Override
        public void doLayout(ScreenRectangle rectangle) {
        }
    }
}