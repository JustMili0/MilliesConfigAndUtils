package net.justmili.libs.v1.config.screen.rows;

import net.justmili.libs.CoreLibs;
import net.justmili.libs.v1.config.entry.ConfigEntry;
import net.justmili.libs.v1.config.screen.ConfigScreenBuilder;
import net.justmili.libs.v1.config.screen.SharedElements;
import net.justmili.libs.v1.config.widgets.BoolButton;
import net.justmili.libs.v1.config.widgets.VarcharBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import static net.justmili.libs.v1.config.screen.SharedElements.*;

@SuppressWarnings({"unchecked", "NullableProblems"})
public class EntryRow extends Row {
    private final String modId;
    private final ConfigScreenBuilder screenBuilder;

    private final ConfigEntry<?> entry;
    private final Consumer<ConfigEntry<?>> onHover;

    private final AbstractWidget widget;

    public EntryRow(ConfigEntry<?> entry, String modId, int depth, Consumer<ConfigEntry<?>> onHover, Deque<Runnable> undoStack, ConfigScreenBuilder list) {
        super(depth);
        this.modId = modId;
        this.entry = entry;
        this.onHover = onHover;
        this.screenBuilder = list;

        if (entry.get() instanceof Boolean) {
            ConfigEntry<Boolean> boolEntry = (ConfigEntry<Boolean>) entry;
            BoolButton[] holder = new BoolButton[1];
            holder[0] = new BoolButton(0, 0, WIDGET_WIDTH, WIDGET_HEIGHT, button -> {
                boolean previous = boolEntry.get();
                boolEntry.set(!previous);
                holder[0].state = !previous;
                undoStack.push(() -> {
                    boolEntry.set(previous);
                    holder[0].state = previous;
                });
            });
            holder[0].state = boolEntry.get();
            this.widget = holder[0];

        } else {
            VarcharBox box = new VarcharBox(Minecraft.getInstance().font, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT);
            box.setValue(String.valueOf(entry.get()));
            box.setFilter(text -> SharedElements.validateInput(entry.defaultValue(), text));
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
        Component label = SharedElements.resolve(SharedElements.varKey(modId, entry.key()));
        if (isHovering) label = label.copy().withStyle(style -> style.withUnderlined(true));

        Font font = Minecraft.getInstance().font;
        int rightEdge = screenBuilder.rowRight(),
            labelX = left+indent()+LABEL_LEFT_PADDING,
            labelY = top+(height-9) / 2,
            labelMaxWidth = rightEdge-WIDGET_WIDTH-WIDGET_RIGHT_MARGIN-LABEL_RIGHT_GAP-labelX,
            widgetX = rightEdge-WIDGET_WIDTH-WIDGET_RIGHT_MARGIN;

        String labelStr = label.getString();
        if (font.width(labelStr) > labelMaxWidth) {
            while (font.width(labelStr+"...") > labelMaxWidth && !labelStr.isEmpty())
                labelStr = labelStr.substring(0, labelStr.length()-1);
            graphics.drawString(font, Component.literal(labelStr+"...").withStyle(label.getStyle()), labelX, labelY, C_WHITE);
        } else {
            graphics.drawString(font, label, labelX, labelY, C_WHITE);
        }

        // Tint label red and italic if the current value is out of the allowed range
        if (widget instanceof VarcharBox box) {
            try {
                Object defaultValue = entry.defaultValue();
                boolean outOfRange = false;
                if (entry.hasRange()) {
                    String text = box.getValue();
                    Comparable<Object> parsed = null;
                    if (defaultValue instanceof Number) {
                        try {
                            if (defaultValue instanceof Integer)
                                parsed = (Comparable<Object>) (Object) Integer.parseInt(text);
                            else if (defaultValue instanceof Long)
                                parsed = (Comparable<Object>) (Object) Long.parseLong(text);
                            else if (defaultValue instanceof Double)
                                parsed = (Comparable<Object>) (Object) Double.parseDouble(text);
                            else if (defaultValue instanceof Float)
                                parsed = (Comparable<Object>) (Object) Float.parseFloat(text);
                        } catch (NumberFormatException e) {
                            outOfRange = true;
                        }
                    }
                    if (parsed != null) outOfRange = parsed.compareTo(entry.min()) < 0 || parsed.compareTo(entry.max()) > 0;
                }
                box.setTextColor(outOfRange ? C_RED : C_WHITE);
            } catch (NumberFormatException ignored) {
                box.setTextColor(C_WHITE);
            }
        }

        widget.setX(widget instanceof BoolButton ? rightEdge-WIDGET_HEIGHT-WIDGET_RIGHT_MARGIN : widgetX);
        widget.setY(top+WIDGET_TOP_MARGIN+1);
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