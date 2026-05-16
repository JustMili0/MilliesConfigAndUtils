package net.justmili.config.core.props;

import net.justmili.config.ConfigLib;
import net.justmili.config.core.FormatWriter;

import java.io.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

public class PropertiesWriter implements FormatWriter {

    @Override
    public void write(Path path, List<EntryInstance> entries) {
        File file = path.toFile();
        file.getParentFile().mkdirs();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("# "+path.getFileName()+"\n\n");
            for (EntryInstance instance : entries) {
                if (instance.comment() != null) writer.write("# "+instance.comment()+"\n");
                writer.write(hint(instance.entry(), CommentStyle.TAG)+"\n");
                writer.write(instance.entry().key()+"="+instance.entry().serialize()+"\n\n");
            }
        } catch (IOException e) {
            ConfigLib.LOGGER.error("Failed to write config: {}", e.getMessage());
        }
    }

    @Override
    public void load(Path path, List<EntryInstance> entries) {
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(path.toFile())) {
            properties.load(inputStream);
        } catch (IOException e) {
            ConfigLib.LOGGER.error("Failed to load config: {}", e.getMessage());
            return;
        }
        for (EntryInstance instance : entries) {
            instance.entry().load(properties.getProperty(instance.entry().key()));
        }
    }
}