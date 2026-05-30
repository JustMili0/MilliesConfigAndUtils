package net.justmili.libs.v1.config.screen.rows;

import net.justmili.libs.v1.config.entry.ListConfigEntry;
import net.justmili.libs.v1.config.screen.ConfigScreenBuilder;
import net.justmili.libs.v1.config.screen.SharedElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
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
import java.util.function.Consumer;

import static net.justmili.libs.v1.config.screen.SharedElements.*;

@SuppressWarnings({"unchecked", "NullableProblems"})
public class ListEntryRow extends Row {
    private final String modId;
    public final ListConfigEntry<?> entry;
    private final ConfigScreenBuilder list;
    private final Consumer<ListConfigEntry<?>> onHover;
    private final Deque<Runnable> undoStack;
    public boolean expanded = false;

    private EditBox inlineBox;
    private Button inlineRemoveBtn;
    private boolean inlinePendingDelete = false;
    private Button inlineConfirmBtn;
    private Button inlineCancelBtn;
    private Button inlineAddBtn;

    public ListEntryRow(ListConfigEntry<?> entry, String modId, int depth, ConfigScreenBuilder list, Consumer<ListConfigEntry<?>> onHover, Deque<Runnable> undoStack) {
        super(depth);
        this.modId = modId;
        this.entry = entry;
        this.list = list;
        this.onHover = onHover;
        this.undoStack = undoStack;
        rebuildInlineWidgets();
    }

    public void rebuildInlineWidgets() {
        inlinePendingDelete = false;

        inlineBox = new EditBox(Minecraft.getInstance().font, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT, Component.empty());
        if (!entry.get().isEmpty()) {
            inlineBox.setValue(String.valueOf(entry.get().get(0)));
            inlineBox.setFilter(text -> SharedElements.validateInput(entry.defaultValue().get(0), text));
            inlineBox.setResponder(text -> {
                try {
                    List current = new ArrayList<>(entry.get());
                    Object previous = current.get(0);
                    Object parsed = parse(entry.defaultValue().get(0), text);
                    if (parsed != null && !parsed.equals(previous)) {
                        current.set(0, parsed);
                        entry.set(current);
                        undoStack.push(() -> {
                            List undo = new ArrayList<>(entry.get());
                            undo.set(0, previous);
                            entry.set(undo);
                            inlineBox.setValue(String.valueOf(previous));
                        });
                    }
                } catch (Exception ignored) {
                }
            });
        }

        inlineRemoveBtn = Button.builder(Component.empty(), btn -> inlinePendingDelete = true)
            .bounds(0, 0, LIST_ICON_BTN_SIZE, LIST_ICON_BTN_SIZE).build();
        inlineRemoveBtn.setAlpha(0f);

        inlineConfirmBtn = Button.builder(Component.empty(), btn -> {
            List current = new ArrayList<>(entry.get());
            Object removed = current.remove(0);
            entry.set(current);
            undoStack.push(() -> {
                List undo = new ArrayList<>(entry.get());
                undo.add(0, removed);
                entry.set(undo);
            });
            list.rebuildFromRoot();
        }).bounds(0, 0, LIST_ICON_BTN_SIZE, LIST_ICON_BTN_SIZE).build();
        inlineConfirmBtn.setAlpha(0f);

        inlineCancelBtn = Button.builder(Component.empty(), btn -> inlinePendingDelete = false)
            .bounds(0, 0, LIST_ICON_BTN_SIZE, LIST_ICON_BTN_SIZE).build();
        inlineCancelBtn.setAlpha(0f);

        inlineAddBtn = Button.builder(Component.empty(), btn -> {
            List current = new ArrayList<>(entry.get());
            Object blank = newBlank(entry.defaultValue().isEmpty() ? null : entry.defaultValue().get(0));
            current.add(blank);
            entry.set(current);
            undoStack.push(() -> {
                List undo = new ArrayList<>(entry.get());
                if (!undo.isEmpty()) undo.remove(undo.size()-1);
                entry.set(undo);
            });
            list.rebuildFromRoot();
        }).bounds(0, 0, LIST_ICON_BTN_SIZE, LIST_ICON_BTN_SIZE).build();
        inlineAddBtn.setAlpha(0f);
    }

    private void toggle() {
        expanded = !expanded;
        list.rebuildFromRoot();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovering, float partialTick) {
        if (isHovering) onHover.accept(entry);

        Font font = Minecraft.getInstance().font;
        int labelX = left+indent()+CAT_LABEL_X_OFFSET,
            labelMaxWidth = width-WIDGET_WIDTH-LABEL_RIGHT_GAP-indent()-CAT_LABEL_X_OFFSET,
            labelY = top+(height-9) / 2;

        Component label = resolve(varKey(modId, entry.key()));
        if (expanded) label = label.copy().withStyle(style -> style.withItalic(true).withUnderlined(true));
        else if (isHovering) label = label.copy().withStyle(style -> style.withUnderlined(true));

        String labelStr = label.getString();
        if (font.width(labelStr) > labelMaxWidth) {
            while (font.width(labelStr+"...") > labelMaxWidth && !labelStr.isEmpty())
                labelStr = labelStr.substring(0, labelStr.length()-1);
            graphics.drawString(font, Component.literal(labelStr+"...").withStyle(label.getStyle()), labelX, labelY, COLOR_WHITE);
        } else {
            graphics.drawString(font, label, labelX, labelY, COLOR_WHITE);
        }

        graphics.renderItem(expanded ? Items.COOKED_PORKCHOP.getDefaultInstance() : Items.PORKCHOP.getDefaultInstance(), left+indent()+CAT_ICON_X_OFFSET, top+(height-CAT_ICON_SIZE) / 2);

        if (expanded) {
            int rightEdge = list.rowRight(),
                btnY = top+(height-LIST_ICON_BTN_SIZE) / 2+2;

            if (entry.get().isEmpty()) {
                int btnX = rightEdge-WIDGET_WIDTH-LIST_ICON_BTN_SIZE-LIST_BTN_GAP * 2-WIDGET_RIGHT_MARGIN+20;
                inlineAddBtn.setX(btnX);
                inlineAddBtn.setY(btnY);
                inlineAddBtn.render(graphics, mouseX, mouseY, partialTick);
                graphics.renderItem(isOver(mouseX, mouseY, btnX, btnY, LIST_ICON_BTN_SIZE)
                    ? Items.SPECTRAL_ARROW.getDefaultInstance()
                    : Items.ARROW.getDefaultInstance(), btnX+LIST_ICON_OFFSET, btnY+LIST_ICON_OFFSET);
            } else if (inlinePendingDelete) {
                int cancelX = rightEdge-WIDGET_WIDTH-LIST_ICON_BTN_SIZE * 2-LIST_BTN_GAP * 3-WIDGET_RIGHT_MARGIN;
                int confirmX = cancelX+LIST_ICON_BTN_SIZE+LIST_BTN_GAP;
                int boxX = confirmX+LIST_ICON_BTN_SIZE+LIST_BTN_GAP;

                inlineCancelBtn.setX(cancelX);
                inlineCancelBtn.setY(btnY);
                inlineCancelBtn.render(graphics, mouseX, mouseY, partialTick);
                graphics.renderItem(isOver(mouseX, mouseY, cancelX, btnY, LIST_ICON_BTN_SIZE)
                    ? Items.RED_STAINED_GLASS_PANE.getDefaultInstance()
                    : Items.BARRIER.getDefaultInstance(), cancelX+LIST_ICON_OFFSET, btnY+LIST_ICON_OFFSET);

                inlineConfirmBtn.setX(confirmX);
                inlineConfirmBtn.setY(btnY);
                inlineConfirmBtn.render(graphics, mouseX, mouseY, partialTick);
                graphics.renderItem(isOver(mouseX, mouseY, confirmX, btnY, LIST_ICON_BTN_SIZE)
                    ? Items.GREEN_STAINED_GLASS_PANE.getDefaultInstance()
                    : Items.SCUTE.getDefaultInstance(), confirmX+LIST_ICON_OFFSET, btnY+LIST_ICON_OFFSET);

                inlineBox.setX(boxX);
                inlineBox.setY(top+WIDGET_TOP_MARGIN);
                inlineBox.setWidth(WIDGET_WIDTH);
                inlineBox.render(graphics, mouseX, mouseY, partialTick);
            } else {
                int removeX = rightEdge-WIDGET_WIDTH-LIST_ICON_BTN_SIZE-LIST_BTN_GAP * 2-WIDGET_RIGHT_MARGIN;
                int boxX = removeX+LIST_ICON_BTN_SIZE+LIST_BTN_GAP;

                inlineRemoveBtn.setX(removeX);
                inlineRemoveBtn.setY(btnY);
                inlineRemoveBtn.render(graphics, mouseX, mouseY, partialTick);
                graphics.renderItem(isOver(mouseX, mouseY, removeX, btnY, LIST_ICON_BTN_SIZE)
                    ? Items.RED_STAINED_GLASS_PANE.getDefaultInstance()
                    : Items.BARRIER.getDefaultInstance(), removeX+LIST_ICON_OFFSET, btnY+LIST_ICON_OFFSET);

                inlineBox.setX(boxX);
                inlineBox.setY(top+WIDGET_TOP_MARGIN);
                inlineBox.setWidth(WIDGET_WIDTH);
                inlineBox.render(graphics, mouseX, mouseY, partialTick);
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (expanded) for (GuiEventListener child : children())
            if (child.isMouseOver(mouseX, mouseY)) {
                child.mouseClicked(mouseX, mouseY, button);
                return true;
            }
        toggle();
        return true;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        if (!expanded) return List.of();
        if (entry.get().isEmpty()) return List.of(inlineAddBtn);
        if (inlinePendingDelete) return List.of(inlineCancelBtn, inlineConfirmBtn, inlineBox);
        return List.of(inlineRemoveBtn, inlineBox);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        if (!expanded) return List.of();
        if (entry.get().isEmpty()) return List.of(inlineAddBtn);
        if (inlinePendingDelete) return List.of(inlineCancelBtn, inlineConfirmBtn, inlineBox);
        return List.of(inlineRemoveBtn, inlineBox);
    }
}