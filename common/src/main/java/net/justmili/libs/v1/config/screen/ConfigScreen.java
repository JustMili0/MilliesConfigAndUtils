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
        ITEM_HEIGHT = 24,
        TAB_HEIGHT = 24,
        BACKGROUND_X_OFFSET = 164,
        PANEL_PADDING = 6,
        PANEL_BUTTON_HEIGHT = 20,
        PANEL_BUTTON_WIDTH = 74,
        PANEL_DIVIDER_X_OFFSET = 1,
        ENTRY_LIST_Y_OFFSET = 2,
        HLINE_Y = 25,
        PREVIEW_TEXT_Y_OFFSET = TAB_HEIGHT+PANEL_PADDING,
        BUTTON_ROW_BOTTOM_OFFSET = PANEL_PADDING,
        BUTTON_ROW_GAP = 4;

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

    private int PANEL_X_OFFSET() {
        return width-BACKGROUND_X_OFFSET;
    }
    private int ENTRY_LIST_WIDTH() {
        return PANEL_X_OFFSET()-PANEL_DIVIDER_X_OFFSET;
    }

    @Override
    protected void init() {
        int entryListWidth = ENTRY_LIST_WIDTH();
        List<Tab> tabs = builders.stream().map(builder -> (Tab) new ConfigTab(builder.getConfig(), this::setActiveConfig)).toList();

        tabBar = TabNavigationBar.builder(tabManager, entryListWidth).addTabs(tabs.toArray(new Tab[0])).build();
        addRenderableWidget(tabBar);
        tabBar.selectTab(0, false);

        int buttonRowY = height-BUTTON_ROW_BOTTOM_OFFSET-PANEL_BUTTON_HEIGHT,
            twoButtonY = buttonRowY-PANEL_BUTTON_HEIGHT-BUTTON_ROW_GAP;

        addRenderableWidget(Button.builder(Component.translatable("gui.config.done"), button -> onClose())
            .bounds(PANEL_X_OFFSET()+PANEL_PADDING, buttonRowY, BACKGROUND_X_OFFSET-PANEL_PADDING * 2, PANEL_BUTTON_HEIGHT)
            .build());

        addRenderableWidget(Button.builder(Component.translatable("gui.config.reset"), button -> {
            if (hoveredEntry == null) return;

            Object previous = hoveredEntry.get();
            ((ConfigEntry<Object>) hoveredEntry).set(hoveredEntry.defaultValue());
            if (entryList != null) entryList.undoStack.push(() -> ((ConfigEntry<Object>) hoveredEntry).set(previous));
        }).bounds(PANEL_X_OFFSET()+PANEL_PADDING, twoButtonY, PANEL_BUTTON_WIDTH, PANEL_BUTTON_HEIGHT).build());

        addRenderableWidget(Button.builder(Component.translatable("gui.config.undo"), button -> {
            if (entryList != null && !entryList.undoStack.isEmpty()) entryList.undoStack.pop().run();
        }).bounds(PANEL_X_OFFSET()+PANEL_PADDING+PANEL_BUTTON_WIDTH+BUTTON_ROW_GAP, twoButtonY, PANEL_BUTTON_WIDTH, PANEL_BUTTON_HEIGHT).build());

        if (!builders.isEmpty()) setActiveConfig(builders.get(0).getConfig());
    }
    private void setActiveConfig(ConfigLoader config) {
        if (entryList != null) removeWidget(entryList);

        int listY = TAB_HEIGHT+ENTRY_LIST_Y_OFFSET,
            listHeight = height-listY;
        entryList = new ConfigScreenBuilder(minecraft, ENTRY_LIST_WIDTH(), listHeight, listY, ITEM_HEIGHT, config,
            entry -> hoveredEntry = entry, category -> {
            hoveredCategory = category;
            hoveredEntry = null;
        });

        addRenderableWidget(entryList);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        hoveredEntry = null;
        hoveredCategory = null;

        renderBackground(graphics);
        graphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
        // Yes, texture size is 31x31 because it matches perfectly, don't ask me why or how, I don't fucking know
        graphics.blit(Screen.BACKGROUND_LOCATION, PANEL_X_OFFSET(), 0, 0, width-10, BACKGROUND_X_OFFSET, height,
            31, 31);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        super.render(graphics, mouseX, mouseY, delta);

        if (tabBar != null) graphics.hLine(0, width, HLINE_Y, SharedElements.COLOR_WHITE);
        graphics.vLine(PANEL_X_OFFSET()-PANEL_DIVIDER_X_OFFSET, TAB_HEIGHT, height, SharedElements.COLOR_WHITE);

        renderPreviewPanel(graphics);
    }
    private void renderPreviewPanel(GuiGraphics graphics) {
        int x = PANEL_X_OFFSET()+PANEL_PADDING,
            y = PREVIEW_TEXT_Y_OFFSET,
            textWidth = BACKGROUND_X_OFFSET-PANEL_PADDING * 2;

        if (hoveredEntry != null) {
            String modId = builders.stream()
                .filter(builder -> builder.getConfig().entries.containsKey(hoveredEntry.key())).findFirst()
                .map(builder -> builder.getConfig().modId).orElse("unknown");

            Component name = SharedElements.resolve(SharedElements.varKey(modId, hoveredEntry.key()))
                .copy().withStyle(style -> style.withBold(true));
            graphics.drawWordWrap(font, name, x, y, textWidth, SharedElements.COLOR_WHITE);
            y += font.wordWrapHeight(name, textWidth);

            graphics.drawWordWrap(font, SharedElements.resolve(
                SharedElements.varDescKey(modId, hoveredEntry.key())), x, y, textWidth, SharedElements.COLOR_WHITE);

        } else if (hoveredCategory != null) {
            String modId = builders.stream()
                .filter(builder -> builder.getConfig().modId != null).findFirst()
                .map(builder -> builder.getConfig().modId).orElse("unknown");

            Component name = SharedElements.resolve(SharedElements.catKey(modId, hoveredCategory.name()))
                .copy().withStyle(style -> style.withBold(true));
            graphics.drawWordWrap(font, name, x, y, textWidth, SharedElements.COLOR_WHITE);
            y += font.wordWrapHeight(name, textWidth);

            graphics.drawWordWrap(font, SharedElements.resolve(
                SharedElements.catDescKey(modId, hoveredCategory.name())), x, y, textWidth, SharedElements.COLOR_WHITE);
        }
    }

    @Override
    public void onClose() {
        if (minecraft != null) minecraft.setScreen(parent);
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