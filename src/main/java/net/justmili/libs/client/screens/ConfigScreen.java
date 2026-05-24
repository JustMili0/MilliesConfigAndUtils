package net.justmili.libs.client.screens;

import net.justmili.libs.config.ConfigLoader;
import net.justmili.libs.config.build.ConfigEntry;
import net.justmili.libs.config.build.MConfigBuilder;
import net.justmili.libs.utils.TranslationKeyUtil;
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
    private static final int PREVIEW_WIDTH = 160;
    private static final int ITEM_HEIGHT = 24;
    private static final int TAB_HEIGHT = 24;
    private static final int PANEL_BUTTON_HEIGHT = 20;
    private static final int PANEL_PADDING = 6;

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
        List<Tab> tabs = builders.stream()
            .map(builder -> (Tab) new ConfigTab(builder.getConfig(), this::setActiveConfig))
            .toList();

        tabBar = TabNavigationBar.builder(tabManager, this.width-PREVIEW_WIDTH).addTabs(tabs.toArray(new Tab[0])).build();
        addRenderableWidget(tabBar);
        tabBar.selectTab(0, false);

        int panelX = this.width-PREVIEW_WIDTH,
            panelBottom = this.height-PANEL_PADDING;

        Button doneButton = Button.builder(Component.literal("Done"), btn -> onClose())
            .bounds(panelX+PANEL_PADDING, panelBottom-PANEL_BUTTON_HEIGHT, PREVIEW_WIDTH-PANEL_PADDING * 2, PANEL_BUTTON_HEIGHT)
            .build();
        addRenderableWidget(doneButton);

        int twoButtonY = panelBottom-PANEL_BUTTON_HEIGHT * 2-4,
            twoButtonW = (PREVIEW_WIDTH-PANEL_PADDING * 3) / 2;

        Button resetButton = Button.builder(Component.literal("Reset"), btn -> {
            if (hoveredEntry == null) return;

            Object previous = hoveredEntry.get();
            ((ConfigEntry<Object>) hoveredEntry).set(hoveredEntry.defaultValue());
            if (entryList != null) entryList.undoStack.push(() -> ((ConfigEntry<Object>) hoveredEntry).set(previous));

        }).bounds(panelX+PANEL_PADDING, twoButtonY, twoButtonW, PANEL_BUTTON_HEIGHT).build();

        addRenderableWidget(resetButton);

        Button undoButton = Button.builder(Component.literal("Undo"), btn -> {
            if (entryList != null && !entryList.undoStack.isEmpty()) entryList.undoStack.pop().run();

        }).bounds(panelX+PANEL_PADDING * 2+twoButtonW, twoButtonY, twoButtonW, PANEL_BUTTON_HEIGHT).build();
        addRenderableWidget(undoButton);

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

        if (tabBar != null) graphics.hLine(0, this.width-PREVIEW_WIDTH, tabBar.getRectangle().bottom(), 0xFFFFFFFF);

        int panelX = this.width-PREVIEW_WIDTH, panelY = 0;
        // Panel background
        graphics.fill(panelX, panelY, this.width, this.height, 0xCC111111);
        // Panel left border
        graphics.fill(panelX, panelY, panelX+1, this.height, 0xFFFFFFFF);

        renderPreviewPanel(graphics, panelX+PANEL_PADDING, panelY+PANEL_PADDING);
    }

    private void renderPreviewPanel(GuiGraphics graphics, int x, int y) {
        int textWidth = PREVIEW_WIDTH-PANEL_PADDING * 2;

        if (hoveredEntry == null) {
            graphics.drawString(font, Component.literal("Hover an entry"), x, y, 0xAAAAAA);
            return;
        }

        String modId = builders.stream()
            .filter(b -> b.getConfig().entries.containsKey(hoveredEntry.key())).findFirst()
            .map(b -> b.getConfig().modId).orElse("unknown");

        // Name
        Component name = Component.translatableWithFallback(TranslationKeyUtil.varKey(modId, hoveredEntry.key()), hoveredEntry.key());
        graphics.drawWordWrap(font, name, x, y, textWidth, 0xFFFFFF);
        y += font.lineHeight+4;

        // Description
        Component desc = Component.translatable(TranslationKeyUtil.varDescKey(modId, hoveredEntry.key()));
        graphics.drawWordWrap(font, desc, x, y, textWidth, 0xAAAAAA);
        y += font.wordWrapHeight(desc, textWidth)+6;

        // Current value
        graphics.drawString(font, Component.literal("Value: "+hoveredEntry.get()), x, y, 0xFFFF55);
        y += font.lineHeight+2;

        // Default value
        graphics.drawString(font, Component.literal("Default: "+hoveredEntry.defaultValue()), x, y, 0xAAAAAA);
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