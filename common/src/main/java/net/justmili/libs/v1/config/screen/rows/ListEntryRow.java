package net.justmili.libs.v1.config.screen.rows;

import net.justmili.libs.v1.config.entry.ListConfigEntry;
import net.justmili.libs.v1.config.screen.ConfigScreenBuilder;
import net.justmili.libs.v1.config.screen.SharedElements;
import net.justmili.libs.v1.config.widgets.VarcharBox;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import static net.justmili.libs.v1.config.screen.SharedElements.*;

@SuppressWarnings({"unchecked", "NullableProblems"})
public class ListEntryRow extends Row {
    private final String modId;
    private final ConfigScreenBuilder list;
    public final ListConfigEntry<?> entry;
    private final Consumer<ListConfigEntry<?>> onHover;
    private final Deque<Runnable> undoStack;

    // Widgets
    private VarcharBox inputBox;
    private Button removeBtn;
    private Button confirmDelBtn;
    private Button cancelDelBtn;
    private Button addBtn;

    // Selection and Hovers
    public boolean expanded = false;
    public boolean inlinePendingDelete = false;

    public ListEntryRow(ListConfigEntry<?> entry, String modId, int depth, ConfigScreenBuilder list,
                        Consumer<ListConfigEntry<?>> onHover, Deque<Runnable> undoStack) {
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

        inputBox = new VarcharBox(Minecraft.getInstance().font, 0, 0, WIDGET_WIDTH, WIDGET_HEIGHT);
        if (!entry.get().isEmpty()) {
            inputBox.setValue(String.valueOf(entry.get().get(0)));
            inputBox.setFilter(text -> SharedElements.validateInput(entry.defaultValue().get(0), text));
            inputBox.setResponder(text -> {
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
                            inputBox.setValue(String.valueOf(previous));
                        });
                    }
                } catch (Exception ignored) {
                }
            });
        }

        removeBtn = Button.builder(Component.empty(), btn -> {
            list.resetAllPendingDeletes();
            inlinePendingDelete = true;
        }).bounds(0, 0, LIST_ICON_BTN_SIZE, LIST_ICON_BTN_SIZE).build();
        removeBtn.setAlpha(0f);

        confirmDelBtn = Button.builder(Component.empty(), btn -> {
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
        confirmDelBtn.setAlpha(0f);

        cancelDelBtn = Button.builder(Component.empty(), btn -> inlinePendingDelete = false)
            .bounds(0, 0, LIST_ICON_BTN_SIZE, LIST_ICON_BTN_SIZE).build();
        cancelDelBtn.setAlpha(0f);

        addBtn = Button.builder(Component.empty(), btn -> {
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
        addBtn.setAlpha(0f);
    }

    private void toggle() {
        expanded = !expanded;
        list.rebuildFromRoot();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int index, int top, int left, int width, int height,
                       int mouseX, int mouseY, boolean isHovering, float partialTick) {
        if (isHovering) onHover.accept(entry);

        Font font = Minecraft.getInstance().font;
        int labelX = left+indent()+CAT_LABEL_X_OFFSET,
            labelY = top+(height-9) / 2,
            labelMaxWidth = width-WIDGET_WIDTH-LABEL_RIGHT_GAP-indent()-CAT_LABEL_X_OFFSET;

        Component label = resolve(varKey(modId, entry.key()));
        if (expanded) label = label.copy().withStyle(style -> style.withItalic(true).withUnderlined(true));
        else if (isHovering) label = label.copy().withStyle(style -> style.withUnderlined(true));

        String labelStr = label.getString();
        
        if (font.width(labelStr) > labelMaxWidth) {
            while (font.width(labelStr+"...") > labelMaxWidth && !labelStr.isEmpty()) 
                labelStr = labelStr.substring(0, labelStr.length()-1);
            
            graphics.drawString(font, Component.literal(labelStr+"...")
                .withStyle(label.getStyle()), labelX, labelY, C_WHITE);
        } else {
            graphics.drawString(font, label, labelX, labelY, C_WHITE);
        }

        renderIcon(graphics, left+indent()+CAT_ICON_X_OFFSET, top+(height-CAT_ICON_SIZE) / 2,
            expanded ? I_LIST_OPEN : I_LIST_CLOSED);

        if (expanded) {
            int rightEdge = list.rowRight(),
                buttonY = top+(height-LIST_ICON_BTN_SIZE) / 2+2;

            if (entry.get().isEmpty()) {
                int buttonX = rightEdge-WIDGET_WIDTH-LIST_ICON_BTN_SIZE-LIST_BTN_GAP * 2-WIDGET_RIGHT_MARGIN+20;

                addHoveredButton(graphics, addBtn, mouseX, mouseY, partialTick, buttonX, buttonY, I_ADD, I_ADD_HOVER);

            } else if (inlinePendingDelete) {
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
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (expanded) {
            if (inputBox.isMouseOver(mouseX, mouseY)) {
                inputBox.setFocused(true);
                inputBox.mouseClicked(mouseX, mouseY, button);
                return true;
            }
            for (GuiEventListener child : children())
                if (child.isMouseOver(mouseX, mouseY)) {
                    child.mouseClicked(mouseX, mouseY, button);
                    return true;
                }
        }
        toggle();
        return true;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        if (!expanded) return List.of();
        if (entry.get().isEmpty()) return List.of(addBtn);
        if (inlinePendingDelete) return List.of(cancelDelBtn, confirmDelBtn, inputBox);
        return List.of(removeBtn, inputBox);
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        if (!expanded) return List.of();
        if (entry.get().isEmpty()) return List.of(addBtn);
        if (inlinePendingDelete) return List.of(cancelDelBtn, confirmDelBtn, inputBox);
        return List.of(removeBtn, inputBox);
    }
}