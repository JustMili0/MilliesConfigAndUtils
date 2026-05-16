package net.justmili.config.core.json;

import net.justmili.config.ConfigLib;
import net.justmili.config.core.FormatWriter;
import net.justmili.config.create.ConfigEntry;

import java.io.*;
import java.nio.file.Path;
import java.util.List;

public class Json5Writer implements FormatWriter {

    @Override
    public void write(Path path, List<EntryInstance> entries) {
        File file = path.toFile();
        file.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("{\n");

            for (int i = 0; i < entries.size(); i++) {
                EntryInstance instance = entries.get(i);
                ConfigEntry<?> entry = instance.entry();
                boolean last = i == entries.size()-1;

                if (instance.comment() != null) {
                    for (String line : instance.comment().split("\n"))
                        writer.write("  // "+line+"\n");
                }

                writer.write("  // "+hint(entry, CommentStyle.NONE)+"\n"); // hint as comment, no prefix from hint itself
                writer.write("  \""+entry.key()+"\": "+SharedJson.jsonValue(entry)+(last ? "\n" : ",\n"));

                if (!last) writer.write("\n");
            }

            writer.write("}\n");

        } catch (IOException e) {
            ConfigLib.LOGGER.error("Failed to write config: {}", e.getMessage());
        }
    }

    @Override
    public void load(Path path, List<EntryInstance> entries) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                String stripped = line.contains("//") ? line.substring(0, line.indexOf("//")) : line;
                stringBuilder.append(stripped).append("\n");
            }

            String json = stringBuilder.toString();

            for (EntryInstance instance : entries) {
                String value = SharedJson.extractValue(json, instance.entry().key());

                if (value != null) instance.entry().load(value);
            }
        } catch (IOException e) {
            ConfigLib.LOGGER.error("Failed to load config: {}", e.getMessage());
        }
    }
}