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

public class Json5Writer implements FormatWriter {

    @Override
    public void write(Path path, CategoryItem root) {
        File file = path.toFile();
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("{\n");
            writeItems(writer, root.children(), "  ");
            writer.write("}\n");
        } catch (IOException e) {
            ConfigLib.LOGGER.error("Failed to write config: {}", e.getMessage());
        }
    }

    private void writeItems(BufferedWriter writer, List<ConfigItem> items, String indent) throws IOException {
        for (int i = 0; i < items.size(); i++) {
            ConfigItem item = items.get(i);
            boolean last = i == items.size()-1;

            if (item instanceof CommentItem commentItem) {
                for (String line : commentItem.comment().split("\n")) writer.write(indent+"// "+line+"\n");

                if (!last) writer.write("\n");
            } else if (item instanceof ConfigEntry<?> entry) {
                writer.write(indent+"// "+hint(entry, CommentStyle.NONE)+"\n");
                writer.write(indent+"\""+entry.key()+"\": "+SharedJson.jsonValue(entry)+(last ? "\n" : ",\n"));

                if (!last) writer.write("\n");
            } else if (item instanceof CategoryItem category) {
                if (category.comment() != null) {
                    for (String line : category.comment().split("\n"))
                        writer.write(indent+"// "+line+"\n");
                }

                writer.write(indent+"\""+category.name()+"\": {\n");
                writeItems(writer, category.children(), indent+"  ");
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
            while ((line = reader.readLine()) != null) {
                String stripped = line.contains("//") ? line.substring(0, line.indexOf("//")) : line;
                stringBuilder.append(stripped).append("\n");
            }

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