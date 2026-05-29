package net.justmili.libs.client.screens;

import net.minecraft.network.chat.Component;

public class ScreenElements {
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
}