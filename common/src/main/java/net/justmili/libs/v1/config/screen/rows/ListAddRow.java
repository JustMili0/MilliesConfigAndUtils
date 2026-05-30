package net.justmili.libs.v1.config.screen.rows;

import net.justmili.libs.v1.config.entry.ListConfigEntry;
import net.justmili.libs.v1.config.screen.ConfigScreenBuilder;
import net.justmili.libs.v1.config.screen.SharedElements;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static net.justmili.libs.v1.config.screen.ConfigScreenBuilder.WIDGET_WIDTH;

public class ListAddRow<T> extends Row {
    private final Button addBtn;
    private static final int ICON_BTN_SIZE = 18;
    private static final int ICON_OFFSET = 1;
    private static final int BTN_GAP = 2;

    public ListAddRow(ListConfigEntry<T> entry, int depth, ConfigScreenBuilder list, Deque<Runnable> undoStack) {
        super(depth);
        this.addBtn = Button.builder(Component.empty(), btn -> {
            List<T> current = new ArrayList<>(entry.get());
            T blank = SharedElements.blankValue(entry.defaultValue().isEmpty() ? null : entry.defaultValue().get(0));
            current.add(blank);
            entry.set(current);
            undoStack.push(() -> {
                List<T> undo = new ArrayList<>(entry.get());
                if (!undo.isEmpty()) undo.remove(undo.size()-1);
                entry.set(undo);
            });
            list.rebuildFromRoot();
        }).bounds(0, 0, ICON_BTN_SIZE, ICON_BTN_SIZE).build();
        addBtn.setAlpha(0f);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovering, float partialTick) {
        int btnX = left+width-WIDGET_WIDTH-ICON_BTN_SIZE-BTN_GAP * 2+20;
        int btnY = top+(height-ICON_BTN_SIZE) / 2+1;
        addBtn.setX(btnX);
        addBtn.setY(btnY);
        addBtn.render(graphics, mouseX, mouseY, partialTick);
        graphics.renderItem(SharedElements.isOver(mouseX, mouseY, btnX, btnY, ICON_BTN_SIZE)
            ? Items.SPECTRAL_ARROW.getDefaultInstance()
            : Items.ARROW.getDefaultInstance(), btnX+ICON_OFFSET, btnY+ICON_OFFSET);
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