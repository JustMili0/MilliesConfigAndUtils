package net.justmili.configscreen;

import net.justmili.corelibs.CoreLibs;
import net.justmili.corelibs.config.Config;
import net.justmili.corelibs.v1.config.screen.widgets.VarcharBox;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Calendar;

@SuppressWarnings({"unchecked"})
public class SharedElements {
    public static final ResourceLocation ICONS = CoreLibs.asResource("textures/gui/icons.png");
    public static final ResourceLocation ICONS_PRIDE = CoreLibs.asResource("textures/gui/icons_pride.png");
    public static final int /// I = Icon, W = Widget, C = Color
        I_CLOSED_CAT = 1, // Arrow right
        I_OPEN_CAT = 2, // Arrow down
        I_LIST_CLOSED = 3, // Vertical dots
        I_LIST_OPEN = 4, // Dotted list
        I_ADD = 5, // Plus
        I_ADD_HOVER = 6, // Plus highlighted
        I_REMOVE = 7, // Minus
        I_REMOVE_HOVER = 8, // Minus highlighted
        I_CANCEL = 9, // X
        I_CANCEL_HOVER = 10, // X highlighted
        I_DELETE = 11, // Trash bin
        I_DELETE_HOVER = 12, // Trash bin highlighted
        I_REORDER = 13, // Burger
        W_BOOL_FALSE = 14, // Empty square
        W_BOOL_TRUE = 15, // Filled square
        W_INPUT_1 = 17, // Underline start
        W_INPUT_2 = 18, // Underline middle
        W_INPUT_3 = 19; // Underline end
    public static final int
        C_WHITE = 0xFFFFFFFF,
        C_RED = 0xFFFB5454,
        C_SEMITRANS_GRAY = 0x55B1A89C,
        C_SEMITRANS_BLACK = 0x80010200;

    // Screen layout
    public static int
        ITEM_HEIGHT = 24,
        TAB_HEIGHT = 24,
        BACKGROUND_X_OFFSET = 164,
        PANEL_PADDING = 6,
        PANEL_BUTTON_HEIGHT = 20,
        PANEL_BUTTON_WIDTH = 74,
        PANEL_DIVIDER_X_OFFSET = 1,
        ENTRY_LIST_Y_OFFSET = 2,
        PREVIEW_TEXT_Y_OFFSET = TAB_HEIGHT+PANEL_PADDING,
        BUTTON_ROW_BOTTOM_OFFSET = PANEL_PADDING,
        BUTTON_ROW_GAP = 4;
    // Row layout
    public static int
        SCROLLBAR_WIDTH = 6,
        SCROLLBAR_MARGIN = 2,
        ROW_WIDTH_REDUCTION = SCROLLBAR_WIDTH+SCROLLBAR_MARGIN-2,
        ROW_LEFT_MARGIN = 2,
        DEPTH_INDENT = 10,
        WIDGET_WIDTH = 150, // Changing this to anything doesn't do shit
        WIDGET_HEIGHT = 20,
        LABEL_LEFT_PADDING = 6,
        LABEL_RIGHT_GAP = 8,
        WIDGET_RIGHT_MARGIN = 4,
        WIDGET_TOP_MARGIN = 2,
        CAT_ICON_SIZE = 20,
        CAT_ICON_X_OFFSET = 4,
        CAT_LABEL_X_OFFSET = 20,
        CAT_LABEL_RIGHT_MARGIN = 30;
    // List row layout
    public static int
        LIST_ICON_BTN_SIZE = 18,
        LIST_ICON_OFFSET = 1,
        LIST_BTN_GAP = 2;

    // Runtime-changing
    public static int
        screenWidth = 0,
        screenHeight = 0,
        panelX = 0,
        entryListWidth = 0,
        buttonRowY = 0,
        twoButtonY = 0,
        listY = 0,
        listHeight = 0;
    public static void updateRuntimeValues(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        panelX = width-BACKGROUND_X_OFFSET;
        entryListWidth = panelX-PANEL_DIVIDER_X_OFFSET;
        buttonRowY = height-BUTTON_ROW_BOTTOM_OFFSET-PANEL_BUTTON_HEIGHT;
        twoButtonY = buttonRowY-PANEL_BUTTON_HEIGHT-BUTTON_ROW_GAP;
        listY = TAB_HEIGHT+ENTRY_LIST_Y_OFFSET;
        listHeight = height-listY;
    }

    public static String toUniform(String input) {
        if (input == null || input.isBlank()) return input;

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == ' ' || c == '-') {
                result.append('_');
            } else if (Character.isUpperCase(c)) {
                if (i > 0 && input.charAt(i-1) != ' ' && input.charAt(i-1) != '-' && input.charAt(i-1) != '_')
                    result.append('_');
                result.append(Character.toLowerCase(c));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    public static String builderKey(String modId, String key) {
        return "config.builder."+modId+"."+toUniform(key)+".name";
    }
    public static String varKey(String modId, String key) {
        return "config.var."+modId+"."+toUniform(key)+".name";
    }
    public static String varDescKey(String modId, String key) {
        return "config.var."+modId+"."+toUniform(key)+".desc";
    }
    public static String catKey(String modId, String name) {
        return "config.cat."+modId+"."+toUniform(name)+".name";
    }
    public static String catDescKey(String modId, String name) {
        return "config.cat."+modId+"."+toUniform(name)+".desc";
    }

    public static Component resolve(String transKey) {
        return Component.translatable(transKey);
    }

    public static boolean validateInput(Object objectValue, String text) {
        if (text.isEmpty()) return true;
        if (objectValue instanceof Integer) return text.matches("-?\\d*");
        if (objectValue instanceof Long) return text.matches("-?\\d*");
        if (objectValue instanceof Double) return text.matches("-?\\d*\\.?\\d*");
        if (objectValue instanceof Float) return text.matches("-?\\d*\\.?\\d*");
        return true;
    }
    public static <T> T newBlank(Object objectValue) {
        if (objectValue instanceof Integer) return (T) Integer.valueOf(0);
        if (objectValue instanceof Long) return (T) Long.valueOf(0L);
        if (objectValue instanceof Double) return (T) Double.valueOf(0.0);
        if (objectValue instanceof Float) return (T) Float.valueOf(0.0f);
        return (T) "";
    }
    public static <T> T parse(Object objectValue, String text) {
        try {
            if (objectValue instanceof Integer) return (T) Integer.valueOf(text);
            if (objectValue instanceof Long) return (T) Long.valueOf(text);
            if (objectValue instanceof Double) return (T) Double.valueOf(text);
            if (objectValue instanceof Float) return (T) Float.valueOf(text);
            return (T) text;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static boolean isHoveredOver(int mouseX, int mouseY, int x, int y, int size) {
        return mouseX >= x && mouseX <= x+size && mouseY >= y && mouseY <= y+size;
    }

    public static void renderHoveredIcon(GuiGraphics graphics, int x, int y, boolean isHoveredOver, int iconIndex, int hoveredIconIndex) {
        renderIcon(graphics, x, y, isHoveredOver ? hoveredIconIndex : iconIndex);
    }
    public static void renderIcon(GuiGraphics graphics, int x, int y, int iconIndex) {
        int placement = iconIndex-1;
        int iconX = (placement % 16) * 16;
        int iconY = (placement / 16) * 16;

        boolean usePrideIcons = (Calendar.getInstance().get(Calendar.MONTH)+1 == 6 || Config.alwaysPride.get()) && !Config.alwaysDefault.get();

        graphics.blit(usePrideIcons ? ICONS_PRIDE : ICONS, x, y, iconX, iconY, 16, 16, 256, 256);
    }

    public static void addHoveredButton(GuiGraphics graphics, Button button, int mouseX, int mouseY,
                                         float partialTick, int buttonX, int buttonY, int iconIndex, int hoveredIconIndex) {
        button.setPosition(buttonX, buttonY);
        button.render(graphics, mouseX, mouseY, partialTick);
        renderHoveredIcon(graphics, buttonX+LIST_ICON_OFFSET-2, buttonY+LIST_ICON_OFFSET-5,
            isHoveredOver(mouseX, mouseY, buttonX, buttonY, LIST_ICON_BTN_SIZE), iconIndex, hoveredIconIndex);
    }
    public static void addInputBox(GuiGraphics graphics, VarcharBox box, int top, int mouseX, int mouseY,
                                   float partialTick, int buttonX) {
        int boxX = buttonX+LIST_ICON_BTN_SIZE+LIST_BTN_GAP;

        box.setPosition(boxX, top+WIDGET_TOP_MARGIN);
        box.setWidth(WIDGET_WIDTH);
        box.render(graphics, mouseX, mouseY, partialTick);
    }
}