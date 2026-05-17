package net.justmili.libs.config.build;

import net.justmili.libs.ConfigLib;
import net.justmili.libs.config.ConfigLoader;
import net.justmili.libs.config.items.CategoryItem;
import net.justmili.libs.config.items.CommentItem;
import net.justmili.libs.config.FileType;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

public class MConfigBuilder {
    private final ConfigLoader config;
    private final Deque<CategoryItem> stack = new ArrayDeque<>();
    private String comment = null;

    public MConfigBuilder(String modId, String name, FileType fileType, boolean createSubDirectory) {
        this.config = new ConfigLoader(modId, name, fileType, createSubDirectory);

        stack.push(new CategoryItem("root", null));
    }

    public MConfigBuilder comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void openCat(String name) {
        CategoryItem category = new CategoryItem(name, comment);
        comment = null;
        stack.peek().add(category);
        stack.push(category);
    }

    public void closeCat() {
        if (stack.size() <= 1) {
            ConfigLib.LOGGER.warn("closeCat() called without a matching openCat(), ignoring.");
            return;
        }
        stack.pop();
    }

    // Lists // TODO: FINISH
    public ConfigEntry<List> define(List key, String defaultValue) {
        return register(new ConfigEntry<>(key, defaultValue, config));
    }

    // String
    public ConfigEntry<String> define(String key, String defaultValue) {
        return register(new ConfigEntry<>(key, defaultValue, config));
    }

    // Boolean
    public ConfigEntry<Boolean> define(String key, boolean defaultValue) {
        return register(new ConfigEntry<>(key, defaultValue, config));
    }

    // Integer
    public ConfigEntry<Integer> define(String key, int defaultValue, int min, int max) {
        return register(new ConfigEntry<>(key, defaultValue, min, max, config));
    }

    // Long
    public ConfigEntry<Long> define(String key, long defaultValue, long min, long max) {
        return register(new ConfigEntry<>(key, defaultValue, min, max, config));
    }

    // Double
    public ConfigEntry<Double> define(String key, double defaultValue, double min, double max) {
        return register(new ConfigEntry<>(key, defaultValue, min, max, config));
    }

    private <T> ConfigEntry<T> register(ConfigEntry<T> entry) {
        if (comment != null) {
            stack.peek().add(new CommentItem(comment));
            comment = null;
        }
        stack.peek().add(entry);
        config.register(entry);
        return entry;
    }

    public void build() {
        if (comment != null) {
            stack.peek().add(new CommentItem(comment));
            comment = null;
        }
        config.loadOrCreate(stack.peek());
    }

    public ConfigLoader getConfig() { return config; }
}