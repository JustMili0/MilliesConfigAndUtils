package net.justmili.libs.utils;

public class TranslationKeyUtil {
    public static String toUniform(String input) {
        if (input == null || input.isBlank()) return input;

        String withUnderscores = input.replace(" ", "_").replace("-", "_");
        if (withUnderscores.equals(withUnderscores.toLowerCase())) return withUnderscores.toLowerCase();

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < withUnderscores.length(); i++) {
            char c = withUnderscores.charAt(i);
            if (Character.isUpperCase(c)) {
                if (i > 0) result.append('_');
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
}