package net.justmili.config.config;

import net.justmili.config.ConfigLib;
import net.justmili.config.create.MConfigBuilder;
import net.justmili.config.create.ConfigEntry;
import net.justmili.config.data.FileType;

public class Config {
    public static ConfigEntry<Integer> someInt;
    public static ConfigEntry<Double> someDouble, someDouble2;
    public static ConfigEntry<String> someString, someOtherString;

    public static void register() {
        MConfigBuilder builder = new MConfigBuilder(ConfigLib.MODID, "example-config", FileType.JSON, true);

        someInt = builder.comment("a comment 1").define("someInt", 5, 0, 10);
        someDouble = builder.comment("a comment 2\na comment continuation (because of a second .comment() or \\n )").define("someDouble", 6.0, 0.0, 12.0);

        builder.comment("A category comment").openCat("someCategory");
        builder.openCat("aCategoryWithNoCommentHenceNoSpacingFromParentCategory");
        someOtherString = builder.define("someOtherString", "Wawawa");
        builder.closeCat();

        builder.comment("A category comment").openCat("aCategoryInCategory");
        someString = builder.comment("a comment 3").define("someString", "Hello world");
        builder.closeCat();

        someDouble2 = builder.comment("a comment 4").define("someDouble2", 8.0, 0.0, 16.0);
        builder.comment("a lonely comment (from just a builder.comment() )");
        builder.closeCat();

        builder.build();
    }
}