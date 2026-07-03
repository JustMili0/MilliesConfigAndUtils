package net.justmili.libs.v1.config.screen.rows;

import net.justmili.libs.v1.config.entry.ListConfigEntry;
import net.justmili.libs.v1.config.screen.ConfigScreenBuilder;
import net.justmili.libs.v1.config.screen.SharedElements;
import net.justmili.libs.v1.config.screen.widgets.VarcharBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static net.justmili.libs.v1.config.screen.SharedElements.*;

@SuppressWarnings({"NullableProblems"})
public class ListElementRow<T> extends Row {
    private final ConfigScreenBuilder screenBuilder;

    private final VarcharBox inputBox;
    private final Button removeBtn;
    private final Button confirmDelBtn;
    private final Button cancelDelBtn;

    public boolean pendingDelete = false;

    public ListElementRow(ListConfigEntry<T> entry, int elementIndex, int depth, ConfigScreenBuilder list, Deque<Runnable> undoStack) {
        super(depth);
        this.screenBuilder = list;

        inputBox = new VarcharBox(Minecraft.getInstance().font, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT);
        inputBox.setValue(String.valueOf(entry.get().get(elementIndex)));
        inputBox.setFilter(text -> SharedElements.validateInput(entry.defaultValue().get(0), text));
        inputBox.setResponder(text -> {
            try {
                List<T> current = new ArrayList<>(entry.get());
                T previous = current.get(elementIndex);
                T parsed = parse(entry.defaultValue().get(0), text);

                if (parsed != null && !parsed.equals(previous)) {
                    current.set(elementIndex, parsed);
                    entry.set(current);

                    undoStack.push(() -> {
                        List<T> undo = new ArrayList<>(entry.get());
                        undo.set(elementIndex, previous);
                        entry.set(undo);
                        inputBox.setValue(String.valueOf(previous));
                    });
                }
            } catch (Exception ignored) {
            }
        });

        removeBtn = Button.builder(Component.empty(), btn -> {
            list.resetAllPendingDeletes();
            pendingDelete = true;
        }).bounds(0, 0, LIST_ICON_BTN_SIZE, LIST_ICON_BTN_SIZE).build();
        removeBtn.setAlpha(0f);

        confirmDelBtn = Button.builder(Component.empty(), btn -> {
            List<T> current = new ArrayList<>(entry.get());
            T removed = current.remove(elementIndex);

            entry.set(current);
            undoStack.push(() -> {
                List<T> undo = new ArrayList<>(entry.get());
                undo.add(elementIndex, removed);
                entry.set(undo);
            });
            list.rebuildFromRoot();

        }).bounds(0, 0, LIST_ICON_BTN_SIZE, LIST_ICON_BTN_SIZE).build();
        confirmDelBtn.setAlpha(0f);

        cancelDelBtn = Button.builder(Component.empty(), btn -> pendingDelete = false)
            .bounds(0, 0, LIST_ICON_BTN_SIZE, LIST_ICON_BTN_SIZE).build();
        cancelDelBtn.setAlpha(0f);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height,
                       int mouseX, int mouseY, boolean isHovering, float partialTick) {
        int rightEdge = screenBuilder.rowRight(),
            buttonY = top+(height-LIST_ICON_BTN_SIZE) / 2+2;

        if (pendingDelete) {
            int cancelX = rightEdge-WIDGET_WIDTH-LIST_ICON_BTN_SIZE * 2-LIST_BTN_GAP * 3-WIDGET_RIGHT_MARGIN,
                confirmX = cancelX+LIST_ICON_BTN_SIZE+LIST_BTN_GAP;

            addHoveredButton(graphics, cancelDelBtn, mouseX, mouseY, partialTick, cancelX, buttonY, I_CANCEL, I_CANCEL_HOVER);
            addHoveredButton(graphics, confirmDelBtn, mouseX, mouseY, partialTick, confirmX, buttonY, I_DELETE, I_DELETE_HOVER);
            addInputBox(graphics, inputBox, top, mouseX, mouseY, partialTick, confirmX);

        } else {
            int removeX = rightEdge-WIDGET_WIDTH-LIST_ICON_BTN_SIZE-LIST_BTN_GAP * 2-WIDGET_RIGHT_MARGIN;

            addHoveredButton(graphics, removeBtn, mouseX, mouseY, partialTick, removeX, buttonY, I_REMOVE, I_REMOVE_HOVER);
            addInputBox(graphics, inputBox, top, mouseX, mouseY, partialTick, removeX);
        }
    }

    @Override
    public List<? extends GuiEventListener> children() {
        if (pendingDelete) return List.of(cancelDelBtn, confirmDelBtn, inputBox);
        return List.of(removeBtn, inputBox);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        if (pendingDelete) return List.of(cancelDelBtn, confirmDelBtn, inputBox);
        return List.of(removeBtn, inputBox);
    }
}