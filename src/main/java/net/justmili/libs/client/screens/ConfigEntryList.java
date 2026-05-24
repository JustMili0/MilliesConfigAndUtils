package net.justmili.libs.client.screens;

import net.justmili.libs.config.ConfigLoader;
import net.justmili.libs.config.build.ConfigEntry;
import net.justmili.libs.config.items.CategoryItem;
import net.justmili.libs.config.items.ConfigItem;
import net.justmili.libs.utils.TranslationKeyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "NullableProblems"})
public class ConfigEntryList extends ContainerObjectSelectionList<ConfigEntryList.Row> {
    private final ConfigLoader config;
    private final Consumer<ConfigEntry<?>> onHover;
    final Deque<Runnable> undoStack = new ArrayDeque<>();

    public ConfigEntryList(Minecraft minecraft, int width, int height, int y, int itemHeight, ConfigLoader config, Consumer<ConfigEntry<?>> onHover) {
        super(minecraft, width, height, y, itemHeight);
        this.config = config;
        this.onHover = onHover;
        buildRows(config.root.children(), 0);
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    private void buildRows(List<ConfigItem> items, int depth) {
        for (ConfigItem item : items) {
            if (item instanceof CategoryItem category) addEntry(new CategoryRow(category, depth, this));
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
            if (item instanceof CategoryItem child) addEntry(new CategoryRow(child, depth, this));
            else if (item instanceof ConfigEntry<?> entry)
                addEntry(new EntryRow(entry, config.modId, depth, onHover, undoStack));
        }
    }

    public abstract static class Row extends ContainerObjectSelectionList.Entry<Row> {
        protected final int depth;
        protected static final int INDENT = 10;

        public Row(int depth) {
            this.depth = depth;
        }

        protected int indent() {
            return depth * INDENT;
        }
    }

    public static class CategoryRow extends Row {
        final CategoryItem category;
        boolean expanded = false;
        private final ConfigEntryList list;

        public CategoryRow(CategoryItem category, int depth, ConfigEntryList list) {
            super(depth);
            this.category = category;
            this.list = list;
        }

        private void toggle() {
            expanded = !expanded;
            List<Row> topRows = new ArrayList<>();
            for (Row row : list.children()) {
                if (row.depth == 0) topRows.add(row);
            }
            list.rebuildPreservingState(topRows);
        }

        @Override
        public boolean mouseClicked(@NotNull MouseButtonEvent event, boolean isDoubleClick) {
            toggle();
            return true;
        }

        @Override
        public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            int x = getX()+indent();
            int y = getContentYMiddle()-4;

            // Placeholder icons
            graphics.renderItem(expanded ? Items.COOKED_BEEF.getDefaultInstance() : Items.BEEF.getDefaultInstance(), x, getY()+2);

            Component label = Component.translatableWithFallback(
                TranslationKeyUtil.catKey(list.config.modId, category.name()), category.name()
            );
            graphics.drawString(Minecraft.getInstance().font, label, x+20, y, isHovering ? 0xFFFFAA : 0xFFFFFF);
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

    public static class EntryRow extends Row {
        final ConfigEntry<?> entry;
        private final String modId;
        private final Consumer<ConfigEntry<?>> onHover;
        private final AbstractWidget widget;
        private static final int WIDGET_WIDTH = 150;
        private static final int WIDGET_HEIGHT = 20;

        public EntryRow(ConfigEntry<?> entry, String modId, int depth, Consumer<ConfigEntry<?>> onHover, Deque<Runnable> undoStack) {
            super(depth);
            this.entry = entry;
            this.modId = modId;
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

        private <T> void applyText(ConfigEntry<T> entry, String text) {
            try {
                Object defaultValue = entry.defaultValue();
                T parsed = switch (defaultValue) {
                    case Integer i -> (T) Integer.valueOf(text);
                    case Long l -> (T) Long.valueOf(text);
                    case Double d -> (T) Double.valueOf(text);
                    case Float f -> (T) Float.valueOf(text);
                    case null, default -> (T) text;
                };
                entry.set(parsed);
            } catch (NumberFormatException ignored) {
            }
        }

        @Override
        public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            if (isHovering) onHover.accept(entry);

            Component label = Component.translatableWithFallback(TranslationKeyUtil.varKey(modId, entry.key()), entry.key());
            graphics.drawString(Minecraft.getInstance().font, label, getX()+indent(), getContentYMiddle()-4, 0xFFFFFF);

            widget.setX(getX()+getWidth()-WIDGET_WIDTH-4);
            widget.setY(getY()+2);
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
}