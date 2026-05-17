package net.justmili.libs.config;

import net.justmili.libs.ConfigLib;
import net.justmili.libs.config.build.ConfigEntry;
import net.justmili.libs.config.build.ListConfigEntry;
import net.justmili.libs.config.items.CategoryItem;
import net.justmili.libs.config.json.Json5Writer;
import net.justmili.libs.config.json.JsonWriter;
import net.justmili.libs.config.props.PropertiesWriter;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {
    private final Path path;
    private final FormatWriter writer;
    private final Map<String, ConfigEntry<?>> entries = new HashMap<>();
    private final Map<String, ListConfigEntry> listEntries = new HashMap<>();
    private CategoryItem root;

    public ConfigLoader(String modId, String name, FileType fileType, boolean createSubDirectory) {
        Path configDirectory = Path.of("config");
        this.writer = resolveWriter(fileType);

        String extension = extension(fileType);
        String fileName = (name == null || name.isBlank())
            ? modId : (createSubDirectory ? name : modId+"-"+name);

        path = createSubDirectory
            ? configDirectory.resolve(modId).resolve(fileName+extension)
            : configDirectory.resolve(fileName+extension);
    }

    public void register(ConfigEntry<?> entry) {
        entries.put(entry.key(), entry);
    }

    public void registerList(ListConfigEntry entry) {
        listEntries.put(entry.key(), entry);
    }

    public void loadOrCreate(CategoryItem root) {
        this.root = root;
        File file = path.toFile();
        if (!file.exists()) {
            ConfigLib.LOGGER.info("No config found, creating defaults.");
            writer.write(path, root);
            return;
        }
        writer.load(path, entries, listEntries);
    }

    public void save() {
        writer.write(path, root);
    }

    private static FormatWriter resolveWriter(FileType fileType) {
        return switch (fileType) {
            case JSON -> new JsonWriter();
            case JSON5 -> new Json5Writer();
            case YAML, YML -> throw new UnsupportedOperationException("YAML/YML support is not yet implemented.");
            default -> new PropertiesWriter();
        };
    }

    private static String extension(FileType fileType) {
        return switch (fileType) {
            case JSON -> ".json";
            case JSON5 -> ".json5";
            case YAML -> ".yaml";
            case YML -> ".yml";
            default -> ".properties";
        };
    }
}