package net.justmili.libs.config;

import net.justmili.libs.config.items.CategoryItem;
import net.justmili.libs.config.build.ConfigEntry;

import java.nio.file.Path;
import java.util.Map;

public interface FormatWriter {
    enum CommentStyle { TAG, SLASH, NONE }

    void write(Path path, CategoryItem root);
    void load(Path path, Map<String, ConfigEntry<?>> entries);

    default String hint(ConfigEntry<?> entry, CommentStyle commentStyle) {
        String prefix = switch (commentStyle) {
            case TAG -> "# ";
            case SLASH -> "// ";
            case NONE -> "";
        };

        Object defaultValue = entry.defaultValue();
        if (entry.hasRange()) return prefix+"Allowed range: "+entry.min()+"-"+entry.max()+" - Default: "+defaultValue;
        if (defaultValue instanceof Boolean) return prefix+"Allowed values: true, false - Default: "+defaultValue;

        return prefix+"Default: "+defaultValue;
    }
}