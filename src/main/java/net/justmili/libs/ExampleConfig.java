package net.justmili.libs;

import net.justmili.libs.config.FileType;
import net.justmili.libs.config.build.ConfigEntry;
import net.justmili.libs.config.build.ListConfigEntry;
import net.justmili.libs.config.build.MConfigBuilder;

import java.util.List;

public class ExampleConfig {
    // Leave your builders exposed if you want to hook your mod to Mod Menu or (Neo)Forge Config...
    public static MConfigBuilder builder = new MConfigBuilder("examplemod", "suffix-or-name", FileType.JSON5, true);

    public static ConfigEntry<Integer> someInt;
    public static ConfigEntry<Float> someFloat;
    public static ConfigEntry<Double> someDouble;
    public static ConfigEntry<Long> someLong;
    public static ConfigEntry<Boolean> someBool;
    public static ConfigEntry<String> someString;
    public static ListConfigEntry<Double> someDoubleList;
    public static ListConfigEntry<String> someStringList;

    public static void register() {
        // ...Or keep it in the register method if you'd like
        // (but you won't be able to integrate your mod with Mod Menu/(Neo)Forge Config)
        // MConfigBuilder builder = new MConfigBuilder("examplemod", "suffix-or-name", FileType.JSON5, true);

        someInt = builder.comment("Integer entry comment")
            .define("someInt", 10, 0, 100);

        builder.openCat("Decimal Numbers");
        someFloat = builder.comment("Float entry comment")
            .define("someFloat", 1.0f, 0.1f, 5.0f);

        someDouble = builder.comment("Double entry comment")
            .define("someDouble", 2.5, 0.0, 20.0);

        someLong = builder.comment("Long entry comment")
            .define("someLong", 123456789L, Long.MIN_VALUE, Long.MAX_VALUE);
        builder.closeCat();

        someBool = builder.comment("Boolean entry comment")
            .define("someBool", true);

        builder.comment("A category comment").openCat("Strings");
        someString = builder.comment("String entry comment")
            .define("someString", "Awawawa");

        builder.openCat("Strings Category");
        someStringList = builder.comment("List entry comment - A string list")
            .defineList("someStringList", List.of("minecraft:grass_block", "minecraft:dirt", "minecraft:stone"));
        builder.closeCat();

        builder.closeCat();

        someDoubleList = builder.comment("List entry comment - A double list")
            .defineList("someDoubleList", List.of(4.2, 6.9, 6.7));

        builder.build(); // Will create, load and update the config file depending on the situation
    }
}