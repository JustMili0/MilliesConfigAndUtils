package net.justmili.libs.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerPlayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static File createFile(String path, String fileName) {
        // Starts in instance root
        // Ex.: "config/millieslibs/", "something.json"
        return new File(path, fileName);
    }
    public static File createPath(String path) {
        return new File(path);
    }
    public static void deleteFileOrPath(Path path) throws Exception {
        Files.deleteIfExists(path);
    }

    public static void readByLineToChat(ServerPlayer player, String assetPath, int initialDelay, int lineDelay) {
        List<String> jsonLines = new ArrayList<>();

        InputStream stream = FileUtil.class.getClassLoader().getResourceAsStream(assetPath);
        if (stream == null) return;
        BufferedReader buffer = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));

        //TXT to JSON
        try {
            String line1;
            while ((line1 = buffer.readLine()) != null) {
                String raw1 = line1.replace("PLAYERNAME", player.getName().getString());
                String line2 = buffer.readLine();
                String raw2 = (line2 != null) ? line2.replace("PLAYERNAME", player.getName().getString()) : "";
                String combined = raw1 + (raw2.isEmpty() ? "" : "\n" + raw2);
                String escaped = combined.replace("\"", "\\\"");
                String json = "{\"text\":\"" + escaped + "\"}";
                jsonLines.add(json);
            }
        } catch (Exception e) {
            e.fillInStackTrace();
            return;
        } finally {
            try { buffer.close(); } catch (Exception ignored) {} }

        TickUtil.waitTicks(initialDelay, () -> {
            for (int i = 0; i < jsonLines.size(); i++) {
                String rawJson = jsonLines.get(i);

                TickUtil.waitTicks(1 + lineDelay * i, () -> {
                    JsonElement element = JsonParser.parseString(rawJson);
                    Component component = ComponentSerialization.CODEC.parse(JsonOps.INSTANCE, element).result().orElse(null);
                    if (component != null) {
                        player.sendSystemMessage(component);
                    }
                });
            }
        });
    }
}