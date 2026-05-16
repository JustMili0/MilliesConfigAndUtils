package net.justmili.config.core;

import net.justmili.config.create.ConfigEntry;

import java.nio.file.Path;
import java.util.List;

public interface FormatWriter {
    record EntryInstance(ConfigEntry<?> entry, String comment) { }
    enum CommentStyle { TAG, SLASH, NONE }

    void write(Path path, List<EntryInstance> entries);
    void load(Path path, List<EntryInstance> entries);

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