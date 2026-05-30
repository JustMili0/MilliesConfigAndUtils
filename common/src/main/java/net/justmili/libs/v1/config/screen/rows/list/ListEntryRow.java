package net.justmili.libs.v1.config.screen.rows.list;

import net.justmili.libs.v1.config.entry.ListConfigEntry;
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

import static net.justmili.libs.v1.config.screen.ConfigScreenBuilder.*;

public class ListEntryRow extends Row {
    private final String modId;
    public final ListConfigEntry<?> entry;
    private final ConfigScreenBuilder list;
    public boolean expanded = false;

    public ListEntryRow(ListConfigEntry<?> entry, String modId, int depth, ConfigScreenBuilder list) {
        super(depth);
        this.modId = modId;
        this.entry = entry;
        this.list = list;
    }

    void toggle() {
        expanded = !expanded;
        list.rebuildFromRoot();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovering, float partialTick) {
        Font font = Minecraft.getInstance().font;
        int labelX = left+indent()+CAT_LABEL_X_OFFSET,
            labelMaxWidth = width-WIDGET_WIDTH-LABEL_RIGHT_GAP-indent()-CAT_LABEL_X_OFFSET,
            labelY = top+(height-9) / 2;

        Component label = SharedElements.resolve(SharedElements.varKey(modId, entry.key()));
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

        graphics.renderItem(expanded ? Items.COOKED_PORKCHOP.getDefaultInstance() : Items.PORKCHOP.getDefaultInstance(), left+indent()+CAT_ICON_X_OFFSET, top+(height-CAT_ICON_SIZE) / 2);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        toggle();
        return true;
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