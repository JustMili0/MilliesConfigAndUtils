package net.justmili.config.config;

import net.justmili.config.ConfigLib;
import net.justmili.config.create.ConfigEntry;
import net.justmili.config.create.MConfigBuilder;
import net.justmili.config.data.FileType;

public class Config {
    public static ConfigEntry<Integer> someInt;
    public static ConfigEntry<Double> someDouble, someDouble2;
    public static ConfigEntry<String> someString, someOtherString;

    public static void register() {
        MConfigBuilder properties = new MConfigBuilder(ConfigLib.MODID, "example-config", FileType.PROPERTIES, true);

        someInt = properties.comment("a comment 1").define("someInt", 5, 0, 10);
        someDouble = properties.comment("a comment 2\na comment continuation (because of a second .comment() or \\n )").define("someDouble", 6.0, 0.0, 12.0);

        properties.comment("A category comment").openCat("someCategory");
        properties.openCat("aCategoryWithNoCommentHenceNoSpacingFromParentCategory");
        someOtherString = properties.define("someOtherString", "Wawawa");
        properties.closeCat();

        properties.comment("A category comment").openCat("aCategoryInCategory");
        someString = properties.comment("a comment 3").define("someString", "Hello world");
        properties.closeCat();

        someDouble2 = properties.comment("a comment 4").define("someDouble2", 8.0, 0.0, 16.0);
        properties.comment("a lonely comment (from just a builder.comment() )");
        properties.closeCat();

        properties.build();

        MConfigBuilder json = new MConfigBuilder(ConfigLib.MODID, "example-config", FileType.JSON, true);

        someInt = json.comment("a comment 1").define("someInt", 5, 0, 10);
        someDouble = json.comment("a comment 2\na comment continuation (because of a second .comment() or \\n )").define("someDouble", 6.0, 0.0, 12.0);

        json.comment("A category comment").openCat("someCategory");
        json.openCat("aCategoryWithNoCommentHenceNoSpacingFromParentCategory");
        someOtherString = json.define("someOtherString", "Wawawa");
        json.closeCat();

        json.comment("A category comment").openCat("aCategoryInCategory");
        someString = json.comment("a comment 3").define("someString", "Hello world");
        json.closeCat();

        someDouble2 = json.comment("a comment 4").define("someDouble2", 8.0, 0.0, 16.0);
        json.comment("a lonely comment (from just a builder.comment() )");
        json.closeCat();

        json.build();

        MConfigBuilder json5 = new MConfigBuilder(ConfigLib.MODID, "example-config", FileType.JSON5, true);

        someInt = json5.comment("a comment 1").define("someInt", 5, 0, 10);
        someDouble = json5.comment("a comment 2\na comment continuation (because of a second .comment() or \\n )").define("someDouble", 6.0, 0.0, 12.0);

        json5.comment("A category comment").openCat("someCategory");
        json5.openCat("aCategoryWithNoCommentHenceNoSpacingFromParentCategory");
        someOtherString = json5.define("someOtherString", "Wawawa");
        json5.closeCat();

        json5.comment("A category comment").openCat("aCategoryInCategory");
        someString = json5.comment("a comment 3").define("someString", "Hello world");
        json5.closeCat();

        someDouble2 = json5.comment("a comment 4").define("someDouble2", 8.0, 0.0, 16.0);
        json5.comment("a lonely comment (from just a builder.comment() )");
        json5.closeCat();

        json5.build();
    }
}