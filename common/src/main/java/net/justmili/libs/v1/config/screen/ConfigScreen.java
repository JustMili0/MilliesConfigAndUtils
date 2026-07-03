package net.justmili.libs.v1.config.screen;

import net.justmili.libs.v1.config.ConfigLoader;
import net.justmili.libs.v1.config.MConfigBuilder;
import net.justmili.libs.v1.config.entry.ConfigEntry;
import net.justmili.libs.v1.config.entry.ListConfigEntry;
import net.justmili.libs.v1.config.items.CategoryItem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
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
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    private ConfigScreenBuilder screenBuilder;
    private final List<MConfigBuilder> builders;

    // Widgets
    private Button doneBtn;
    private Button resetBtn;
    private Button undoBtn;

    // Selection and Hovers
    private ConfigEntry<?> hoveredEntry = null;
    private ListConfigEntry<?> hoveredList = null;
    private CategoryItem hoveredCategory = null;
    private ConfigEntry<?> selectedEntry = null;
    private ListConfigEntry<?> selectedList = null;

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

        // Build Builder Tabs
        List<Tab> tabs = builders.stream().map(builder -> (Tab) new ConfigTab(builder.getConfig(), this::setActiveConfig)).toList();
        TabNavigationBar tabNavigationBar = TabNavigationBar.builder(tabManager, width).addTabs(tabs.toArray(new Tab[0])).build();
        addRenderableWidget(tabNavigationBar);
        tabNavigationBar.selectTab(0, false);
        tabNavigationBar.arrangeElements();

        // Build buttons
        doneBtn = Button.builder(Component.translatable("gui.config.done"), button -> onClose())
            .bounds(panelX+PANEL_PADDING, buttonRowY, BACKGROUND_X_OFFSET-PANEL_PADDING * 2, PANEL_BUTTON_HEIGHT)
            .build();

        undoBtn = Button.builder(Component.translatable("gui.config.undo"), button -> {
                if (screenBuilder != null && !screenBuilder.undoStack.isEmpty()) screenBuilder.undoStack.pop().run();
            }).bounds(panelX+PANEL_PADDING+PANEL_BUTTON_WIDTH+BUTTON_ROW_GAP, twoButtonY, PANEL_BUTTON_WIDTH, PANEL_BUTTON_HEIGHT)
            .tooltip(Tooltip.create(Component.translatable("gui.config.undo.desc")))
            .build();
        undoBtn.active = false;

        resetBtn = Button.builder(Component.translatable("gui.config.reset"), button -> {
                if (selectedEntry == null) return;
                Object previous = selectedEntry.get();
                ((ConfigEntry<Object>) selectedEntry).set(selectedEntry.defaultValue());
                if (screenBuilder != null) screenBuilder.undoStack.push(() -> ((ConfigEntry<Object>) selectedEntry).set(previous));
            }).bounds(panelX+PANEL_PADDING, twoButtonY, PANEL_BUTTON_WIDTH, PANEL_BUTTON_HEIGHT)
            .tooltip(Tooltip.create(Component.translatable("gui.config.reset.desc")))
            .build();
        resetBtn.active = false;

        addRenderableWidget(doneBtn);
        addRenderableWidget(undoBtn);
        addRenderableWidget(resetBtn);

        // Build config
        if (!builders.isEmpty()) setActiveConfig(builders.get(0).getConfig());
    }

    private void setActiveConfig(ConfigLoader config) {
        if (screenBuilder != null) removeWidget(screenBuilder);
        selectedEntry = null;
        selectedList = null;

        screenBuilder = new ConfigScreenBuilder(minecraft, entryListWidth, listHeight, listY, ITEM_HEIGHT, config,
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

        addRenderableWidget(screenBuilder);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        hoveredEntry = null;
        hoveredList = null;
        hoveredCategory = null;

        // Draw background
        graphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
        graphics.blit(Screen.BACKGROUND_LOCATION, 0, TAB_HEIGHT, 1, width, width, height-TAB_HEIGHT, 32, 32);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        super.render(graphics, mouseX, mouseY, delta);

        // Update button states
        if (resetBtn != null) resetBtn.active = selectedEntry != null;
        if (undoBtn != null) undoBtn.active = screenBuilder != null && !screenBuilder.undoStack.isEmpty();

        // Draw borderlines splitting apart the screen into the Config Panel and the Preview Panel
        graphics.vLine(panelX-PANEL_DIVIDER_X_OFFSET, TAB_HEIGHT-2, height, C_SEMITRANS_GRAY);
        graphics.vLine(panelX-PANEL_DIVIDER_X_OFFSET+1, TAB_HEIGHT-2, height, C_SEMITRANS_BLACK);

        // Render preview panel
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
            drawPreviewForKey(graphics, varKey(modId, hoveredEntry.key()), varDescKey(modId, hoveredEntry.key()), x, y, textWidth);

        } else if (hoveredCategory != null) {
            String modId = builders.stream()
                .filter(builder -> builder.getConfig().modId != null).findFirst()
                .map(builder -> builder.getConfig().modId).orElse("unknown");
            drawPreviewForKey(graphics, catKey(modId, hoveredCategory.name()), catDescKey(modId, hoveredCategory.name()), x, y, textWidth);

        } else if (hoveredList != null) {
            String modId = builders.stream()
                .filter(builder -> builder.getConfig().listEntries.containsKey(hoveredList.key())).findFirst()
                .map(builder -> builder.getConfig().modId).orElse("unknown");
            drawPreviewForKey(graphics, varKey(modId, hoveredList.key()), varDescKey(modId, hoveredList.key()), x, y, textWidth);
        }
    }

    private void drawPreviewForKey(GuiGraphics graphics, String nameKey, String descKey, int x, int y, int textWidth) {
        Component name = resolve(nameKey).copy().withStyle(style -> style.withBold(true));
        graphics.drawWordWrap(font, name, x, y, textWidth, C_WHITE);
        y += font.wordWrapHeight(name, textWidth);
        graphics.drawWordWrap(font, resolve(descKey), x, y, textWidth, C_WHITE);
    }

    @Override
    public void onClose() {
        if (minecraft != null) minecraft.setScreen(parent);
    }

    private record ConfigTab(ConfigLoader config, Consumer<ConfigLoader> onSelect) implements Tab {
        @Override
        public Component getTabTitle() {
            return resolve(builderKey(config.modId, config.suffix != null && !config.suffix.isBlank() ? config.suffix : config.modId));
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