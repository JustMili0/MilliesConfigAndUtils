package net.justmili.libs.client.screens;

import net.justmili.libs.config.ConfigLoader;
import net.justmili.libs.config.build.ConfigEntry;
import net.justmili.libs.config.items.CategoryItem;
import net.justmili.libs.config.items.ConfigItem;
import net.justmili.libs.utils.TranslationKeyUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfigEntryList extends ContainerObjectSelectionList<ConfigEntryList.Row> {
    private final ConfigLoader config;
    private final Consumer<ConfigEntry<?>> onHover;

    public ConfigEntryList(Minecraft minecraft, int width, int height, int y, int itemHeight, ConfigLoader config, Consumer<ConfigEntry<?>> onHover) {
        super(minecraft, width, height, y, itemHeight);
        this.config = config;
        this.onHover = onHover;
        buildRows(config.root.children(), 0);
    }

    private void buildRows(List<ConfigItem> items, int depth) {
        for (ConfigItem item : items) {
            if (item instanceof CategoryItem category) addEntry(new CategoryRow(category, depth, this));
            else if (item instanceof ConfigEntry<?> entry) addEntry(new EntryRow(entry, config.modId, depth, onHover));
            // CommentItems are file-only, skip
        }
    }

    void rebuildPreservingState(List<Row> topRows) {
        clearEntries();
        for (Row row : topRows) {
            addEntry(row);
            if (row instanceof CategoryRow catRow && catRow.expanded)
                addChildRows(catRow.category, catRow.depth+1);
        }
    }

    private void addChildRows(CategoryItem category, int depth) {
        for (ConfigItem item : category.children()) {
            if (item instanceof CategoryItem child) addEntry(new CategoryRow(child, depth, this));
            else if (item instanceof ConfigEntry<?> entry) addEntry(new EntryRow(entry, config.modId, depth, onHover));
        }
    }

    public abstract static class Row extends ContainerObjectSelectionList.Entry<Row> {
        protected final int depth;
        protected static final int INDENT = 10;

        public Row(int depth) {
            this.depth = depth;
        }

        protected int indent() { return depth * INDENT; }
    }

    public static class CategoryRow extends Row {
        final CategoryItem category;
        boolean expanded = false;
        private final Button toggleButton;
        private final ConfigEntryList list;

        public CategoryRow(CategoryItem category, int depth, ConfigEntryList list) {
            super(depth);
            this.category = category;
            this.list = list;
            this.toggleButton = Button.builder(
                Component.literal(prefix()+" "+category.name()),
                btn -> toggle()
            ).bounds(0, 0, 200, 20).build();
        }

        private String prefix() { return expanded ? "[-]" : "[+]"; }

        private void toggle() {
            expanded = !expanded;
            toggleButton.setMessage(Component.literal(prefix()+" "+category.name()));

            List<Row> topRows = new ArrayList<>();
            for (Row row : list.children()) {
                if (row.depth == 0) topRows.add(row);
            }
            list.rebuildPreservingState(topRows);
        }

        @Override
        public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean isHovering, float partialTick) {
            toggleButton.setX(getX()+indent());
            toggleButton.setY(getY());
            toggleButton.setWidth(getWidth()-indent());
            toggleButton.render(graphics, mouseX, mouseY, partialTick);
        }

        @Override
        public List<? extends GuiEventListener> children() { return List.of(toggleButton); }

        @Override
        public List<? extends NarratableEntry> narratables() { return List.of(toggleButton); }
    }

    public static class EntryRow extends Row {
        final ConfigEntry<?> entry;
        private final String modId;
        private final Consumer<ConfigEntry<?>> onHover;
        private final net.minecraft.client.gui.components.AbstractWidget widget;
        private static final int WIDGET_WIDTH = 150;
        private static final int WIDGET_HEIGHT = 20;

        @SuppressWarnings("unchecked")
        public EntryRow(ConfigEntry<?> entry, String modId, int depth, Consumer<ConfigEntry<?>> onHover) {
            super(depth);
            this.entry = entry;
            this.modId = modId;
            this.onHover = onHover;

            if (entry.get() instanceof Boolean) {
                ConfigEntry<Boolean> boolEntry = (ConfigEntry<Boolean>) entry;
                this.widget = Button.builder(
                    Component.literal(Boolean.toString(boolEntry.get())),
                    btn -> {
                        boolEntry.set(!boolEntry.get());
                        btn.setMessage(Component.literal(Boolean.toString(boolEntry.get())));
                    }
                ).bounds(0, 0, WIDGET_WIDTH, WIDGET_HEIGHT).build();
            } else {
                EditBox box = new EditBox(Minecraft.getInstance().font, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT, Component.empty());
                box.setValue(String.valueOf(entry.get()));
                box.setResponder(text -> applyText((ConfigEntry<Object>) entry, text));
                this.widget = box;
            }
        }

        @SuppressWarnings("unchecked")
        private <T> void applyText(ConfigEntry<T> entry, String text) {
            try {
                Object def = entry.defaultValue();
                T parsed;
                if (def instanceof Integer) parsed = (T) Integer.valueOf(text);
                else if (def instanceof Long) parsed = (T) Long.valueOf(text);
                else if (def instanceof Double) parsed = (T) Double.valueOf(text);
                else if (def instanceof Float) parsed = (T) Float.valueOf(text);
                else parsed = (T) text;
                entry.set(parsed);
            } catch (NumberFormatException ignored) {}
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
        public List<? extends GuiEventListener> children() { return List.of(widget); }

        @Override
        public List<? extends NarratableEntry> narratables() { return List.of(widget); }
    }
}