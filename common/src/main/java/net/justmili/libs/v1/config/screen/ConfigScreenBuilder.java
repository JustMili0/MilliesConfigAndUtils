package net.justmili.libs.v1.config.screen;

import net.justmili.libs.CoreLibs;
import net.justmili.libs.v1.config.ConfigLoader;
import net.justmili.libs.v1.config.entry.ConfigEntry;
import net.justmili.libs.v1.config.items.CategoryItem;
import net.justmili.libs.v1.config.items.ConfigItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "NullableProblems"})
public class ConfigScreenBuilder extends ContainerObjectSelectionList<ConfigScreenBuilder.Row> {
    private static final int
        SCROLLBAR_WIDTH = 6,
        SCROLLBAR_MARGIN = 2,
        ROW_WIDTH_REDUCTION = SCROLLBAR_WIDTH+SCROLLBAR_MARGIN-2,
        ROW_LEFT_MARGIN = 2,
        DEPTH_INDENT = 10,
        WIDGET_WIDTH = 150,
        WIDGET_HEIGHT = 20,
        LABEL_LEFT_PADDING = 6,
        LABEL_RIGHT_GAP = 8,
        WIDGET_RIGHT_MARGIN = 4,
        WIDGET_TOP_MARGIN = 2,
        CAT_ICON_SIZE = 20,
        CAT_ICON_X_OFFSET = 4,
        CAT_LABEL_X_OFFSET = 20,
        CAT_LABEL_RIGHT_MARGIN = 30;

    private final ConfigLoader config;
    private final Consumer<ConfigEntry<?>> onHover;
    private final Consumer<CategoryItem> onCatHover;
    public final Deque<Runnable> undoStack = new ArrayDeque<>();

    public ConfigScreenBuilder(Minecraft minecraft, int width, int height, int y, int itemHeight,
                               ConfigLoader config, Consumer<ConfigEntry<?>> onHover, Consumer<CategoryItem> onCatHover) {
        super(minecraft, width, height, y, y+height, itemHeight);
        this.config = config;
        this.onHover = onHover;
        this.onCatHover = onCatHover;
        if (config.root == null) {
            CoreLibs.LOGGER.error("Config '{}' has no root - config file(s) may not have been loaded. " +
                "Please ensure your config class is registered during mod init.", config.modId);
            return;
        }
        buildRows(config.root.children(), 0);
    }

    private void buildRows(List<ConfigItem> items, int depth) {
        for (ConfigItem item : items) {
            if (item instanceof CategoryItem category) addEntry(new CategoryRow(category, depth, onCatHover, this));
            else if (item instanceof ConfigEntry<?> entry)
                addEntry(new EntryRow(entry, config.modId, depth, onHover, undoStack));
            // CommentItems are file-only, skip
        }
    }

    private void rebuildPreservingState(List<Row> topRows) {
        clearEntries();
        for (Row row : topRows) {
            addEntry(row);
            if (row instanceof CategoryRow catRow && catRow.expanded) addChildRows(catRow.category, catRow.depth+1);
        }
    }

    private void addChildRows(CategoryItem category, int depth) {
        for (ConfigItem item : category.children()) {
            if (item instanceof CategoryItem child) addEntry(new CategoryRow(child, depth, onCatHover, this));
            else if (item instanceof ConfigEntry<?> entry)
                addEntry(new EntryRow(entry, config.modId, depth, onHover, undoStack));
        }
    }

    @Override
    public int getRowWidth() {
        return this.width-ROW_WIDTH_REDUCTION;
    }

    @Override
    public int getRowLeft() {
        return this.x0+ROW_LEFT_MARGIN;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.x0+this.width-SCROLLBAR_WIDTH;
    }

    public abstract static class Row extends ContainerObjectSelectionList.Entry<Row> {
        protected final int depth;

        public Row(int depth) {
            this.depth = depth;
        }

        protected int indent() {
            return depth * DEPTH_INDENT;
        }
    }

    // Entries
    public static class EntryRow extends Row {
        private final String modId;
        private final ConfigEntry<?> entry;
        private final Consumer<ConfigEntry<?>> onHover;
        private final AbstractWidget widget;

        public EntryRow(ConfigEntry<?> entry, String modId, int depth, Consumer<ConfigEntry<?>> onHover, Deque<Runnable> undoStack) {
            super(depth);
            this.modId = modId;
            this.entry = entry;
            this.onHover = onHover;

            if (entry.get() instanceof Boolean) {
                ConfigEntry<Boolean> boolEntry = (ConfigEntry<Boolean>) entry;
                this.widget = Button.builder(
                    Component.literal(Boolean.toString(boolEntry.get())),
                    button -> {
                        boolean previous = boolEntry.get();
                        boolEntry.set(!previous);
                        button.setMessage(Component.literal(Boolean.toString(boolEntry.get())));
                        undoStack.push(() -> {
                            boolEntry.set(previous);
                            button.setMessage(Component.literal(Boolean.toString(previous)));
                        });
                    }
                ).bounds(0, 0, WIDGET_WIDTH, WIDGET_HEIGHT).build();

            } else {
                EditBox box = new EditBox(Minecraft.getInstance().font, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT, Component.empty());
                box.setValue(String.valueOf(entry.get()));
                box.setFilter(text -> validateInput(entry.defaultValue(), text));
                box.setResponder(text -> {
                    Object previous = entry.get();
                    applyText((ConfigEntry<Object>) entry, text);
                    if (!entry.get().equals(previous))
                        undoStack.push(() -> {
                            ((ConfigEntry<Object>) entry).set(previous);
                            box.setValue(String.valueOf(previous));
                        });
                });
                this.widget = box;
            }
        }

        private static boolean validateInput(Object defaultValue, String text) {
            if (text.isEmpty()) return true;
            if (defaultValue instanceof Integer) return text.matches("-?\\d*");
            if (defaultValue instanceof Long) return text.matches("-?\\d*");
            if (defaultValue instanceof Double) return text.matches("-?\\d*\\.?\\d*");
            if (defaultValue instanceof Float) return text.matches("-?\\d*\\.?\\d*");
            return true;
        }

        private <T> void applyText(ConfigEntry<T> entry, String text) {
            try {
                Object defaultValue = entry.defaultValue();
                T parsed;
                if (defaultValue instanceof Integer) parsed = (T) Integer.valueOf(text);
                else if (defaultValue instanceof Long) parsed = (T) Long.valueOf(text);
                else if (defaultValue instanceof Double) parsed = (T) Double.valueOf(text);
                else if (defaultValue instanceof Float) parsed = (T) Float.valueOf(text);
                else parsed = (T) text;
                entry.set(parsed);
            } catch (NumberFormatException ignored) {
            }
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height,
                           int mouseX, int mouseY, boolean isHovering, float partialTick) {
            if (isHovering) onHover.accept(entry);
            Component label = ScreenElements.resolve(ScreenElements.varKey(modId, entry.key()));
            if (isHovering) label = label.copy().withStyle(style -> style.withUnderlined(true));

            Font font = Minecraft.getInstance().font;
            int labelX = left+indent()+LABEL_LEFT_PADDING,
                labelMaxWidth = width-WIDGET_WIDTH-LABEL_RIGHT_GAP-indent(),
                labelY = top+(height-9)/2;

            String labelStr = label.getString();
            if (font.width(labelStr) > labelMaxWidth) {
                while (font.width(labelStr+"...") > labelMaxWidth && !labelStr.isEmpty())
                    labelStr = labelStr.substring(0, labelStr.length()-1);
                graphics.drawString(font, Component.literal(labelStr+"...").withStyle(label.getStyle()), labelX, labelY, ScreenElements.COLOR_WHITE);
            } else {
                graphics.drawString(font, label, labelX, labelY, ScreenElements.COLOR_WHITE);
            }

            widget.setX(left+width-WIDGET_WIDTH-WIDGET_RIGHT_MARGIN);
            widget.setY(top+WIDGET_TOP_MARGIN);
            widget.render(graphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of(widget);
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of(widget);
        }
    }

    // Categories
    public static class CategoryRow extends Row {
        final CategoryItem category;
        private final Consumer<CategoryItem> onCatHover;
        private final ConfigScreenBuilder list;
        boolean expanded = false;

        public CategoryRow(CategoryItem category, int depth, Consumer<CategoryItem> onCatHover, ConfigScreenBuilder list) {
            super(depth);
            this.category = category;
            this.onCatHover = onCatHover;
            this.list = list;
        }

        private void toggle() {
            expanded = !expanded;
            List<Row> topRows = new ArrayList<>();
            for (Row row : list.children()) {
                if (row.depth == depth) topRows.add(row);
            }
            list.rebuildPreservingState(topRows);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            toggle();
            return true;
        }

        @Override
        public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height,
                           int mouseX, int mouseY, boolean isHovering, float partialTick) {
            if (isHovering) onCatHover.accept(category);
            int x = left+indent();

            Font font = Minecraft.getInstance().font;
            int labelX = x+CAT_LABEL_X_OFFSET,
                labelMaxWidth = width-indent()-CAT_LABEL_RIGHT_MARGIN,
                labelY = top+(height-9)/2;

            // Placeholder icons
            graphics.renderItem(expanded ? Items.COOKED_BEEF.getDefaultInstance() : Items.BEEF.getDefaultInstance(),
                x+CAT_ICON_X_OFFSET, top+(height-CAT_ICON_SIZE)/2);
            Component label = ScreenElements.resolve(ScreenElements.catKey(list.config.modId, category.name()));

            if (expanded) label = label.copy().withStyle(style -> style.withItalic(true).withUnderlined(true));
            else if (isHovering) label = label.copy().withStyle(style -> style.withUnderlined(true));

            String labelStr = label.getString();
            if (font.width(labelStr) > labelMaxWidth) {
                while (font.width(labelStr+"...") > labelMaxWidth && !labelStr.isEmpty())
                    labelStr = labelStr.substring(0, labelStr.length()-1);
                graphics.drawString(font, Component.literal(labelStr+"...").withStyle(label.getStyle()), labelX, labelY, ScreenElements.COLOR_WHITE);
            } else {
                graphics.drawString(font, label, labelX, labelY, ScreenElements.COLOR_WHITE);
            }
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of();
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of();
        }
    }
}