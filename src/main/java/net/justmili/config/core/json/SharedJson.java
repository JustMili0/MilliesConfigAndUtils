package net.justmili.config.core.json;

import net.justmili.config.create.ConfigEntry;

public class SharedJson {
    public static String extractValue(String json, String key) {
        String search = "\""+key+"\"";

        int index = json.indexOf(search), colon = json.indexOf(':', index+search.length());
        if (index == -1 || colon == -1) return null;

        int start = colon+1;
        while (start < json.length() && json.charAt(start) == ' ') start++;

        if (start >= json.length()) return null;
        char first = json.charAt(start);
        if (first == '"') {
            int end = json.indexOf('"', start+1);
            return end == -1 ? null : json.substring(start+1, end);
        }

        int end = start;
        while (end < json.length() && json.charAt(end) != ',' && json.charAt(end) != '\n' && json.charAt(end) != '}') end++;

        return json.substring(start, end).trim();
    }

    public static String jsonValue(ConfigEntry<?> entry) {
        Object value = entry.get();
        if (value instanceof String) return "\""+value+"\"";

        return String.valueOf(value);
    }
}
