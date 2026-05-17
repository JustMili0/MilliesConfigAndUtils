package net.justmili.config.core.json;

import net.justmili.config.ConfigLib;
import net.justmili.config.core.FormatWriter;
import net.justmili.config.core.items.CategoryItem;
import net.justmili.config.core.items.CommentItem;
import net.justmili.config.core.items.ConfigItem;
import net.justmili.config.create.ConfigEntry;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class JsonWriter implements FormatWriter {

    @Override
    public void write(Path path, CategoryItem root) {
        File file = path.toFile();
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("{\n");
            writeItems(writer, root.children(), "  ", new int[]{0});
            writer.write("}\n");
        } catch (IOException e) {
            ConfigLib.LOGGER.error("Failed to write config: {}", e.getMessage());
        }
    }

    private void writeItems(BufferedWriter writer, List<ConfigItem> items, String indent, int[] hintCounter) throws IOException {
        for (int i = 0; i < items.size(); i++) {
            ConfigItem item = items.get(i);
            boolean last = i == items.size()-1;

            if (item instanceof CommentItem) {
                // JSON doesn't support comments, skip silently
            } else if (item instanceof ConfigEntry<?> entry) {
                writer.write(indent+"\"h"+hintCounter[0]+++"\": \""+hint(entry, CommentStyle.NONE)+"\",\n");
                writer.write(indent+"\""+entry.key()+"\": "+SharedJson.jsonValue(entry)+(last ? "\n" : ",\n"));
                if (!last) writer.write("\n");
            } else if (item instanceof CategoryItem category) {
                writer.write(indent+"\""+category.name()+"\": {\n");
                writeItems(writer, category.children(), indent+"  ", hintCounter);
                writer.write(indent+"}"+(last ? "\n" : ",\n"));
                if (!last) writer.write("\n");
            }
        }
    }

    @Override
    public void load(Path path, Map<String, ConfigEntry<?>> entries) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) stringBuilder.append(line).append("\n");

            String json = stringBuilder.toString();
            for (ConfigEntry<?> entry : entries.values()) {
                String value = SharedJson.extractValue(json, entry.key());
                if (value != null) entry.load(value);
            }
        } catch (IOException e) {
            ConfigLib.LOGGER.error("Failed to load config: {}", e.getMessage());
        }
    }
}