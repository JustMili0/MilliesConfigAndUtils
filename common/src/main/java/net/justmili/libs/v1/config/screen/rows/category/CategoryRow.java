package net.justmili.libs.v1.config.screen.rows.category;

import net.justmili.libs.v1.config.items.CategoryItem;
import net.justmili.libs.v1.config.screen.ConfigScreenBuilder;
import net.justmili.libs.v1.config.screen.SharedElements;
import net.justmili.libs.v1.config.screen.rows.Row;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Consumer;

import static net.justmili.libs.v1.config.screen.ConfigScreenBuilder.*;

public class CategoryRow extends Row {
    public final CategoryItem category;
    private final Consumer<CategoryItem> onCatHover;
    private final ConfigScreenBuilder list;
    public boolean expanded = false;

    public CategoryRow(CategoryItem category, int depth, Consumer<CategoryItem> onCatHover, ConfigScreenBuilder list) {
        super(depth);
        this.category = category;
        this.onCatHover = onCatHover;
        this.list = list;
    }

    private void toggle() {
        expanded = !expanded;
        list.rebuildFromRoot();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        toggle();
        return true;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovering, float partialTick) {
        if (isHovering) onCatHover.accept(category);
        int x = left+indent();

        Font font = Minecraft.getInstance().font;
        int labelX = x+CAT_LABEL_X_OFFSET,
            labelMaxWidth = width-indent()-CAT_LABEL_RIGHT_MARGIN,
            labelY = top+(height-9) / 2;

        // Placeholder icons
        graphics.renderItem(expanded ? Items.COOKED_BEEF.getDefaultInstance() : Items.BEEF.getDefaultInstance(), x+CAT_ICON_X_OFFSET, top+(height-CAT_ICON_SIZE) / 2);
        Component label = SharedElements.resolve(SharedElements.catKey(list.config.modId, category.name()));

        if (expanded) label = label.copy().withStyle(style -> style.withItalic(true).withUnderlined(true));
        else if (isHovering) label = label.copy().withStyle(style -> style.withUnderlined(true));

        String labelStr = label.getString();
        if (font.width(labelStr) > labelMaxWidth) {
            while (font.width(labelStr+"...") > labelMaxWidth && !labelStr.isEmpty())
                labelStr = labelStr.substring(0, labelStr.length()-1);
            graphics.drawString(font, Component.literal(labelStr+"...").withStyle(label.getStyle()), labelX, labelY, SharedElements.COLOR_WHITE);
        } else {
            graphics.drawString(font, label, labelX, labelY, SharedElements.COLOR_WHITE);
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