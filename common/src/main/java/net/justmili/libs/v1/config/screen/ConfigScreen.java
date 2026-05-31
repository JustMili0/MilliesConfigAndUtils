package net.justmili.libs.v1.config.screen;

import net.justmili.libs.v1.config.ConfigLoader;
import net.justmili.libs.v1.config.MConfigBuilder;
import net.justmili.libs.v1.config.entry.ConfigEntry;
import net.justmili.libs.v1.config.entry.ListConfigEntry;
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

import static net.justmili.libs.v1.config.screen.SharedElements.*;

@SuppressWarnings({"unchecked", "NullableProblems"})
public class ConfigScreen extends Screen {
    public final Screen parent;
    private final List<MConfigBuilder> builders;
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private TabNavigationBar tabBar;
    private ConfigScreenBuilder entryList;
    private ConfigEntry<?> hoveredEntry = null;
    private ListConfigEntry<?> hoveredList = null;
    private CategoryItem hoveredCategory = null;

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
        updateRuntimeValues(width, height);

        List<Tab> tabs = builders.stream().map(builder -> (Tab) new ConfigTab(builder.getConfig(), this::setActiveConfig)).toList();
        tabBar = TabNavigationBar.builder(tabManager, width).addTabs(tabs.toArray(new Tab[0])).build();
        addRenderableWidget(tabBar);
        tabBar.selectTab(0, false);
        tabBar.arrangeElements();

        addRenderableWidget(Button.builder(Component.translatable("gui.config.done"), button -> onClose())
            .bounds(panelX+PANEL_PADDING, buttonRowY, BACKGROUND_X_OFFSET-PANEL_PADDING * 2, PANEL_BUTTON_HEIGHT)
            .build());

        addRenderableWidget(Button.builder(Component.translatable("gui.config.reset"), button -> {
            if (hoveredEntry == null) return;

            Object previous = hoveredEntry.get();
            ((ConfigEntry<Object>) hoveredEntry).set(hoveredEntry.defaultValue());
            if (entryList != null) entryList.undoStack.push(() -> ((ConfigEntry<Object>) hoveredEntry).set(previous));

        }).bounds(panelX+PANEL_PADDING, twoButtonY, PANEL_BUTTON_WIDTH, PANEL_BUTTON_HEIGHT).build());

        addRenderableWidget(Button.builder(Component.translatable("gui.config.undo"), button -> {
            if (entryList != null && !entryList.undoStack.isEmpty()) entryList.undoStack.pop().run();

        }).bounds(panelX+PANEL_PADDING+PANEL_BUTTON_WIDTH+BUTTON_ROW_GAP, twoButtonY, PANEL_BUTTON_WIDTH, PANEL_BUTTON_HEIGHT).build());

        if (!builders.isEmpty()) setActiveConfig(builders.get(0).getConfig());
    }

    private void setActiveConfig(ConfigLoader config) {
        if (entryList != null) removeWidget(entryList);

        entryList = new ConfigScreenBuilder(minecraft, entryListWidth, listHeight, listY, ITEM_HEIGHT, config,
            entry -> hoveredEntry = entry,
            category -> {
                hoveredCategory = category;
                hoveredEntry = null;
            },
            listEntry -> {
                hoveredList = listEntry;
                hoveredEntry = null;
                hoveredCategory = null;
            });

        addRenderableWidget(entryList);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        hoveredEntry = null;
        hoveredList = null;
        hoveredCategory = null;

        graphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
        // Yes, texture size is 31x31 because it matches perfectly, don't ask me why or how, I don't fucking know
        graphics.blit(Screen.BACKGROUND_LOCATION, 0, TAB_HEIGHT, 1, width, width, height-TAB_HEIGHT, 32, 32);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        super.render(graphics, mouseX, mouseY, delta);

        graphics.vLine(panelX-PANEL_DIVIDER_X_OFFSET, TAB_HEIGHT-2, height, COLOR_SEMITRANS_GRAY);
        graphics.vLine(panelX-PANEL_DIVIDER_X_OFFSET+1, TAB_HEIGHT-2, height, COLOR_SEMITRANS_BLACK);

        renderPreviewPanel(graphics);
    }

    private void renderPreviewPanel(GuiGraphics graphics) {
        int x = panelX+PANEL_PADDING,
            y = PREVIEW_TEXT_Y_OFFSET,
            textWidth = BACKGROUND_X_OFFSET-PANEL_PADDING * 2;

        if (hoveredEntry != null) {
            String modId = builders.stream()
                .filter(builder -> builder.getConfig().entries.containsKey(hoveredEntry.key())).findFirst()
                .map(builder -> builder.getConfig().modId).orElse("unknown");

            Component name = resolve(varKey(modId, hoveredEntry.key()))
                .copy().withStyle(style -> style.withBold(true));
            graphics.drawWordWrap(font, name, x, y, textWidth, COLOR_WHITE);
            y += font.wordWrapHeight(name, textWidth);

            graphics.drawWordWrap(font, resolve(varDescKey(modId, hoveredEntry.key())), x, y, textWidth, COLOR_WHITE);

        } else if (hoveredCategory != null) {
            String modId = builders.stream()
                .filter(builder -> builder.getConfig().modId != null).findFirst()
                .map(builder -> builder.getConfig().modId).orElse("unknown");

            Component name = resolve(catKey(modId, hoveredCategory.name()))
                .copy().withStyle(style -> style.withBold(true));
            graphics.drawWordWrap(font, name, x, y, textWidth, COLOR_WHITE);
            y += font.wordWrapHeight(name, textWidth);

            graphics.drawWordWrap(font, resolve(catDescKey(modId, hoveredCategory.name())), x, y, textWidth, COLOR_WHITE);

        } else if (hoveredList != null) {
            String modId = builders.stream()
                .filter(builder -> builder.getConfig().listEntries.containsKey(hoveredList.key())).findFirst()
                .map(builder -> builder.getConfig().modId).orElse("unknown");

            Component name = resolve(varKey(modId, hoveredList.key()))
                .copy().withStyle(style -> style.withBold(true));
            graphics.drawWordWrap(font, name, x, y, textWidth, COLOR_WHITE);
            y += font.wordWrapHeight(name, textWidth);

            graphics.drawWordWrap(font, resolve(varDescKey(modId, hoveredList.key())), x, y, textWidth, COLOR_WHITE);
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