package net.justmili.libs.config.build;

import net.justmili.libs.Library;
import net.justmili.libs.config.ConfigLoader;
import net.justmili.libs.config.items.ConfigItem;

import java.util.ArrayList;
import java.util.List;

public class ListConfigEntry implements ConfigItem {
    private final String key;
    private final List<Object> defaultValue;
    private List<Object> value;
    private final Class<?> allowedType;
    private final ConfigLoader config;

    public ListConfigEntry(String key, List<Object> defaultValue, Class<?> allowedType, ConfigLoader config) {
        this.key = key;
        this.defaultValue = new ArrayList<>(defaultValue);
        this.value = new ArrayList<>(defaultValue);
        this.allowedType = allowedType;
        this.config = config;
    }

    public String key() {
        return key;
    }

    public List<Object> get() {
        return value;
    }

    public List<Object> defaultValue() {
        return defaultValue;
    }

    public Class<?> allowedType() {
        return allowedType;
    }

    public boolean is(List<Object> other) {
        return value.equals(other);
    }

    public void set(List<Object> newValue) {
        if (!validate(newValue)) return;
        this.value = new ArrayList<>(newValue);
        config.save();
    }

    private boolean validate(List<Object> list) {
        for (Object element : list) {
            if (!allowedType.isInstance(element)) {
                Library.LOGGER.warn("List '{}' contains invalid type '{}', expected '{}', ignoring.",
                    key, element.getClass().getSimpleName(), allowedType.getSimpleName());
                return false;
            }
        }
        return true;
    }

    public void load(String raw) {
        if (raw == null || raw.isBlank()) return;
        List<Object> parsed = new ArrayList<>();

        for (String element : raw.split(",")) {
            String trimmed = element.trim();
            try {
                parsed.add(parseElement(trimmed));
            } catch (Exception e) {
                Library.LOGGER.warn("Failed to parse list element '{}' for key '{}', skipping.", trimmed, key);
            }
        }

        value = parsed.isEmpty() ? new ArrayList<>(defaultValue) : parsed;
    }

    public String serialize() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < value.size(); i++) {
            stringBuilder.append(value.get(i));
            if (i < value.size()-1) stringBuilder.append(",");
        }
        return stringBuilder.toString();
    }

    public String serializeJson(String indent) {
        StringBuilder stringBuilder = new StringBuilder("[\n");
        for (int i = 0; i < value.size(); i++) {
            Object element = value.get(i);
            String formatted = element instanceof String ? "\""+element+"\"" : String.valueOf(element);
            stringBuilder.append(indent+"  ").append(formatted);
            if (i < value.size()-1) stringBuilder.append(",");
            stringBuilder.append("\n");
        }
        stringBuilder.append(indent+"]");
        return stringBuilder.toString();
    }

    private Object parseElement(String raw) {
        if (allowedType == Integer.class) return Integer.parseInt(raw);
        if (allowedType == Long.class) return Long.parseLong(raw);
        if (allowedType == Double.class) return Double.parseDouble(raw);
        if (allowedType == Float.class) return Float.parseFloat(raw);
        if (allowedType == Boolean.class) return Boolean.parseBoolean(raw);
        return raw;
    }
}