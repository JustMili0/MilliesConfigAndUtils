package net.justmili.libs.v1.config.screen.rows.list;

import net.justmili.libs.v1.config.entry.ListConfigEntry;
import net.justmili.libs.v1.config.screen.ConfigScreenBuilder;
import net.justmili.libs.v1.config.screen.SharedElements;
import net.justmili.libs.v1.config.screen.rows.Row;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static net.justmili.libs.v1.config.screen.ConfigScreenBuilder.*;

public class ListElementRow<T> extends Row {
    private final EditBox box;
    private boolean pendingDelete = false;
    private final Button removeBtn;
    private final Button confirmBtn;
    private final Button cancelBtn;
    private static final int ICON_BTN_SIZE = 18;
    private static final int ICON_OFFSET = 1;
    private static final int BTN_GAP = 2;

    public ListElementRow(ListConfigEntry<T> entry, int elementIndex, int depth, ConfigScreenBuilder list, Deque<Runnable> undoStack) {
        super(depth);

        box = new EditBox(Minecraft.getInstance().font, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT, Component.empty());
        box.setValue(String.valueOf(entry.get().get(elementIndex)));
        box.setFilter(text -> SharedElements.validateInput(entry.defaultValue().get(0), text));
        box.setResponder(text -> {
            try {
                List<T> current = new ArrayList<>(entry.get());
                T previous = current.get(elementIndex);
                T parsed = parseElement(entry.defaultValue().get(0), text);
                if (parsed != null && !parsed.equals(previous)) {
                    current.set(elementIndex, parsed);
                    entry.set(current);
                    undoStack.push(() -> {
                        List<T> undo = new ArrayList<>(entry.get());
                        undo.set(elementIndex, previous);
                        entry.set(undo);
                        box.setValue(String.valueOf(previous));
                    });
                }
            } catch (Exception ignored) {
            }
        });

        removeBtn = Button.builder(Component.empty(), btn -> pendingDelete = true)
            .bounds(0, 0, ICON_BTN_SIZE, ICON_BTN_SIZE).build();
        removeBtn.setAlpha(0f);

        confirmBtn = Button.builder(Component.empty(), btn -> {
            List<T> current = new ArrayList<>(entry.get());
            T removed = current.remove(elementIndex);
            entry.set(current);
            undoStack.push(() -> {
                List<T> undo = new ArrayList<>(entry.get());
                undo.add(elementIndex, removed);
                entry.set(undo);
            });
            list.rebuildFromRoot();
        }).bounds(0, 0, ICON_BTN_SIZE, ICON_BTN_SIZE).build();
        confirmBtn.setAlpha(0f);

        cancelBtn = Button.builder(Component.empty(), btn -> pendingDelete = false)
            .bounds(0, 0, ICON_BTN_SIZE, ICON_BTN_SIZE).build();
        cancelBtn.setAlpha(0f);
    }

    private static <T> T parseElement(Object sample, String text) {
        try {
            if (sample instanceof Integer) return (T) Integer.valueOf(text);
            if (sample instanceof Long) return (T) Long.valueOf(text);
            if (sample instanceof Double) return (T) Double.valueOf(text);
            if (sample instanceof Float) return (T) Float.valueOf(text);
            return (T) text;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static boolean isOver(int mouseX, int mouseY, int x, int y, int size) {
        return mouseX >= x && mouseX <= x+size && mouseY >= y && mouseY <= y+size;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovering, float partialTick) {
        int btnY = top+(height-ICON_BTN_SIZE) / 2+2;

        if (pendingDelete) {
            int cancelX = left+width-WIDGET_WIDTH-ICON_BTN_SIZE * 2-BTN_GAP * 3;
            int confirmX = cancelX+ICON_BTN_SIZE+BTN_GAP;
            int boxX = confirmX+ICON_BTN_SIZE+BTN_GAP;

            cancelBtn.setX(cancelX);
            cancelBtn.setY(btnY);
            cancelBtn.render(graphics, mouseX, mouseY, partialTick);
            graphics.renderItem(isOver(mouseX, mouseY, cancelX, btnY, ICON_BTN_SIZE)
                ? Items.RED_STAINED_GLASS_PANE.getDefaultInstance()
                : Items.BARRIER.getDefaultInstance(), cancelX+ICON_OFFSET, btnY+ICON_OFFSET);

            confirmBtn.setX(confirmX);
            confirmBtn.setY(btnY);
            confirmBtn.render(graphics, mouseX, mouseY, partialTick);
            graphics.renderItem(isOver(mouseX, mouseY, confirmX, btnY, ICON_BTN_SIZE)
                ? Items.GREEN_STAINED_GLASS_PANE.getDefaultInstance()
                : Items.SCUTE.getDefaultInstance(), confirmX+ICON_OFFSET, btnY+ICON_OFFSET);

            box.setX(boxX);
            box.setY(top+WIDGET_TOP_MARGIN);
            box.setWidth(WIDGET_WIDTH);
            box.render(graphics, mouseX, mouseY, partialTick);
        } else {
            int removeX = left+width-WIDGET_WIDTH-ICON_BTN_SIZE-BTN_GAP * 2;
            int boxX = removeX+ICON_BTN_SIZE+BTN_GAP;

            removeBtn.setX(removeX);
            removeBtn.setY(btnY);
            removeBtn.render(graphics, mouseX, mouseY, partialTick);
            graphics.renderItem(isOver(mouseX, mouseY, removeX, btnY, ICON_BTN_SIZE)
                ? Items.RED_STAINED_GLASS_PANE.getDefaultInstance()
                : Items.BARRIER.getDefaultInstance(), removeX+ICON_OFFSET, btnY+ICON_OFFSET);

            box.setX(boxX);
            box.setY(top+WIDGET_TOP_MARGIN);
            box.setWidth(WIDGET_WIDTH);
            box.render(graphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public List<? extends GuiEventListener> children() {
        if (pendingDelete) return List.of(cancelBtn, confirmBtn, box);
        return List.of(removeBtn, box);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        if (pendingDelete) return List.of(cancelBtn, confirmBtn, box);
        return List.of(removeBtn, box);
    }
}