package net.justmili.libs.v1.config.screen;

import net.justmili.libs.CoreLibs;
import net.justmili.libs.v1.config.ConfigLoader;
import net.justmili.libs.v1.config.entry.ConfigEntry;
import net.justmili.libs.v1.config.entry.ListConfigEntry;
import net.justmili.libs.v1.config.items.CategoryItem;
import net.justmili.libs.v1.config.items.ConfigItem;
import net.justmili.libs.v1.config.screen.rows.*;
import net.justmili.libs.v1.config.screen.rows.CategoryRow;
import net.justmili.libs.v1.config.screen.rows.EntryRow;
import net.justmili.libs.v1.config.screen.rows.ListAddRow;
import net.justmili.libs.v1.config.screen.rows.ListElementRow;
import net.justmili.libs.v1.config.screen.rows.ListEntryRow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings({"unchecked", "NullableProblems"})
public class ConfigScreenBuilder extends ContainerObjectSelectionList<Row> {
    public static final int
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

    public final ConfigLoader config;
    private final Consumer<ConfigEntry<?>> onHover;
    private final Consumer<CategoryItem> onCatHover;
    public final Deque<Runnable> undoStack = new ArrayDeque<>();

    public ConfigScreenBuilder(Minecraft minecraft, int width, int height, int y, int itemHeight, ConfigLoader config, Consumer<ConfigEntry<?>> onHover, Consumer<CategoryItem> onCatHover) {
        super(minecraft, width, height, y, y+height, itemHeight);
        this.config = config;
        this.onHover = onHover;
        this.onCatHover = onCatHover;
        if (config.root == null) {
            CoreLibs.LOGGER.error("Config '{}' has no root - config file(s) may not have been loaded. Please ensure your config class is registered during mod init.", config.modId);
            return;
        }
        buildRows(config.root.children(), 0);
    }

    private void buildRows(List<ConfigItem> items, int depth) {
        buildRowsPreservingExpansion(items, depth, new ArrayList<>(), new ArrayList<>());
    }

    private void buildRowsPreservingExpansion(List<ConfigItem> items, int depth, List<CategoryRow> expandedCats, List<ListEntryRow> expandedLists) {
        for (ConfigItem item : items) {
            if (item instanceof CategoryItem category) {
                CategoryRow existing = expandedCats.stream()
                    .filter(r -> r.category == category)
                    .findFirst().orElse(null);
                CategoryRow row = existing != null ? existing : new CategoryRow(category, depth, onCatHover, this);
                addEntry(row);
                if (row.expanded)
                    buildRowsPreservingExpansion(category.children(), depth+1, expandedCats, expandedLists);
            } else if (item instanceof ConfigEntry<?> entry) {
                addEntry(new EntryRow(entry, config.modId, depth, onHover, undoStack));
            } else if (item instanceof ListConfigEntry<?> listEntry) {
                ListEntryRow existing = expandedLists.stream()
                    .filter(r -> r.entry == listEntry)
                    .findFirst().orElse(null);
                ListEntryRow row = existing != null ? existing : new ListEntryRow(listEntry, config.modId, depth, this);
                addEntry(row);
                if (row.expanded) {
                    for (int i = 0; i < listEntry.get().size(); i++)
                        addEntry(new ListElementRow<>(listEntry, i, depth+1, this, undoStack));
                    addEntry(new ListAddRow<>(listEntry, depth+1, this, undoStack));
                }
            }
            // CommentItems are file-only, skip
        }
    }

    public void rebuildFromRoot() {
        List<CategoryRow> expandedCats = new ArrayList<>();
        List<ListEntryRow> expandedLists = new ArrayList<>();
        for (Row row : children()) {
            if (row instanceof CategoryRow catRow && catRow.expanded) expandedCats.add(catRow);
            if (row instanceof ListEntryRow listRow && listRow.expanded) expandedLists.add(listRow);
        }
        clearEntries();
        buildRowsPreservingExpansion(config.root.children(), 0, expandedCats, expandedLists);
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
}