package net.justmili.libs.v1.config.screen;

import net.minecraft.network.chat.Component;

public class SharedElements {
    public static final int COLOR_WHITE = 0xFFFFFFFF;

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
    public static <T> T blankValue(Object sample) {
        if (sample instanceof Integer) return (T) Integer.valueOf(0);
        if (sample instanceof Long) return (T) Long.valueOf(0L);
        if (sample instanceof Double) return (T) Double.valueOf(0.0);
        if (sample instanceof Float) return (T) Float.valueOf(0.0f);
        return (T) "";
    }

    public static boolean isOver(int mouseX, int mouseY, int x, int y, int size) {
        return mouseX >= x && mouseX <= x+size && mouseY >= y && mouseY <= y+size;
    }
}