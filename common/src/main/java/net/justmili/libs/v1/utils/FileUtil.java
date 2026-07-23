package net.justmili.libs.v1.utils;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {
    public static File createFile(String path, String fileName) {
        // Starts in instance root
        // Ex.: "config/corelibs/", "something.json"
        return new File(path, fileName);
    }

    public static File createPath(String path) {
        return new File(path);
    }

    public static void deleteFileOrPath(Path path) throws Exception {
        try { Files.deleteIfExists(path); } catch (Exception ignored) {}
    }
}