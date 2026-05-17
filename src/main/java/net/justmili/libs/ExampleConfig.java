package net.justmili.libs;

import net.justmili.libs.config.FileType;
import net.justmili.libs.config.build.ConfigEntry;
import net.justmili.libs.config.build.ListConfigEntry;
import net.justmili.libs.config.build.MConfigBuilder;

import java.util.List;

public class ExampleConfig {
    public static ConfigEntry<Integer> someInt, newIntInCatNoComment;
    public static ConfigEntry<Double> someDouble;
    public static ConfigEntry<Boolean> someBool, newBoolWithComment;
    public static ConfigEntry<String> someString, someOtherString, newStringInCatWithComment;
    public static ListConfigEntry someStringList;

    public static void register() {
        for (FileType fileType : new FileType[]{FileType.PROPERTIES, FileType.JSON, FileType.JSON5}) {
            MConfigBuilder builder = new MConfigBuilder(Library.MODID, "example-config", fileType, true);

            someInt = builder.comment("a comment 1").define("someInt", 5, 0, 10);
            someBool = builder.comment("a comment 2\na comment continuation (because of a second .comment() or \\n )")
                .define("someBool", true);
            someStringList = builder.comment("a string list").defineList("someStringList", List.of("item1", "item2", "item3"), String.class);

            // someDoubleList removed

            newBoolWithComment = builder.comment("a new bool with a comment").define("newBoolWithComment", false);

            builder.comment("A category comment").openCat("someCategory");
            builder.openCat("aCategoryWithNoCommentHenceNoSpacingFromParentCategory");
            // someOtherString moved out, someString moved in
            someString = builder.comment("a comment 3").define("someString", "Hello world");
            newIntInCatNoComment = builder.define("newIntInCatNoComment", 42, 0, 100);
            builder.closeCat();

            builder.comment("A category comment").openCat("aCategoryInCategory");
            // someString moved out, someOtherString moved in
            someOtherString = builder.define("someOtherString", "Wawawa");
            newStringInCatWithComment = builder.comment("a new string in cat").define("newStringInCatWithComment", "brand new");
            builder.closeCat();

            someDouble = builder.comment("a comment 4").define("someDouble", 8.0, 0.0, 16.0);
            builder.comment("a lonely comment (from just a builder.comment() )");
            builder.closeCat();

            builder.build();
        }
    }
}