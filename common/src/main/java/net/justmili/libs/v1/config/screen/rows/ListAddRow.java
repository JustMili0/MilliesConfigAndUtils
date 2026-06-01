package net.justmili.libs.v1.config.screen.rows;

import net.justmili.libs.v1.config.entry.ListConfigEntry;
import net.justmili.libs.v1.config.screen.ConfigScreenBuilder;
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
public class ListAddRow<T> extends Row {
    private final Button addBtn;
    private final ConfigScreenBuilder list;

    public ListAddRow(ListConfigEntry<T> entry, int depth, ConfigScreenBuilder list, Deque<Runnable> undoStack) {
        super(depth);
        this.list = list;
        this.addBtn = Button.builder(Component.empty(), btn -> {
            List<T> current = new ArrayList<>(entry.get());
            T blank = newBlank(entry.defaultValue().isEmpty() ? null : entry.defaultValue().get(0));
            current.add(blank);
            entry.set(current);
            undoStack.push(() -> {
                List<T> undo = new ArrayList<>(entry.get());
                if (!undo.isEmpty()) undo.remove(undo.size()-1);
                entry.set(undo);
            });
            list.rebuildFromRoot();
        }).bounds(0, 0, LIST_ICON_BTN_SIZE, LIST_ICON_BTN_SIZE).build();
        addBtn.setAlpha(0f);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height,
                       int mouseX, int mouseY, boolean isHovering, float partialTick) {
        int rightEdge = list.rowRight(),
            btnX = rightEdge-WIDGET_WIDTH-LIST_ICON_BTN_SIZE-LIST_BTN_GAP * 2-WIDGET_RIGHT_MARGIN+20,
            btnY = top+(height-LIST_ICON_BTN_SIZE) / 2+1;

        addBtn.setX(btnX);
        addBtn.setY(btnY);
        addBtn.render(graphics, mouseX, mouseY, partialTick);
        renderHoveredIcon(graphics, isOver(mouseX, mouseY, btnX, btnY, LIST_ICON_BTN_SIZE),
            I_ADD, I_ADD_HOVER, btnX+LIST_ICON_OFFSET, btnY+LIST_ICON_OFFSET);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return List.of(addBtn);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return List.of(addBtn);
    }
}