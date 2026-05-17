package net.justmili.config.core.props;

import net.justmili.config.ConfigLib;
import net.justmili.config.core.FormatWriter;
import net.justmili.config.core.items.CategoryItem;
import net.justmili.config.core.items.CommentItem;
import net.justmili.config.core.items.ConfigItem;
import net.justmili.config.create.ConfigEntry;

import java.io.*;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;

public class PropertiesWriter implements FormatWriter {

    @Override
    public void write(Path path, CategoryItem root) {
        File file = path.toFile();
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# "+path.getFileName()+"\n\n");
            writeItems(writer, root);
        } catch (IOException e) {
            ConfigLib.LOGGER.error("Failed to write config: {}", e.getMessage());
        }
    }

    private void writeItems(BufferedWriter writer, CategoryItem category) throws IOException {
        for (ConfigItem item : category.children()) {
            if (item instanceof CategoryItem) {
                ConfigLib.LOGGER.warn("Categories are not supported in .properties format, skipping category '{}'.", ((CategoryItem) item).name());
            } else if (item instanceof CommentItem commentItem) {
                writer.write("# "+commentItem.comment()+"\n\n");
            } else if (item instanceof ConfigEntry<?> entry) {
                writer.write(hint(entry, CommentStyle.TAG)+"\n");
                writer.write(entry.key()+"="+entry.serialize()+"\n\n");
            }
        }
    }

    @Override
    public void load(Path path, Map<String, ConfigEntry<?>> entries) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(path.toFile())) {
            properties.load(inputStream);
        } catch (IOException e) {
            ConfigLib.LOGGER.error("Failed to load config: {}", e.getMessage());
            return;
        }
        for (ConfigEntry<?> entry : entries.values()) {
            entry.load(properties.getProperty(entry.key()));
        }
    }
}