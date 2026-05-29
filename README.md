# Millie's Core Libraries
The all-in-one for you.

<hr>

## Why?
Well there's a couple reasons
- Architectury API kinda sucks, it's good usually but has some weird quirks and takes its sweet time to get updated
- Most Config libraries either don't have everything I need or are a pain to work with
- I hate having more than 2-3 dependencies

## Dev Notes
After all projects by [Lumynity Studios](https://modrinth.com/organization/lumynity-studios) will be updated to latest versions and loaders as well as my mod [Left Forgotten](https://modrinth.com/mod/left-forgotten), this library will no longer support Minecraft 1.20.1!

### Planned version-loader support?
- 26.2.x - Fabric
- 26.1.x - Fabric, NeoForge
- 1.21.11 - Fabric, NeoForge
- (1.21.6-)1.21.8 - Fabric, NeoForge
- 1.21.5 - Fabric, NeoForge
- 1.21.4 - Fabric, NeoForge
- 1.21.1 - Fabric, NeoForge
- 1.20.1 - Fabric/Quilt, Forge <- We are here

<hr>

## Documentation
All documentation can be found under [docs.lumynitystudios.net/corelibs](https://docs.lumynitystudios.net/corelibs)<br>
(URL may change as the website at the time of writing this is not yet made)

<!-- TO BE DELETED -->
**Config Library - Example Config**<br>
<details>
<summary>How to Add to Gradle Project</summary>

### In Kotlin (build.gradle.kts)

```kotlin

repositories {
    maven("https://api.modrinth.com/maven")
}

dependencies {
    // Replace [VERSION] with whatever latest version is available 
    // for the Minecraft version and loader you need
    modImplementation("maven.modrinth:millies-config-n-utils:[VERSION]")
}

```

### In Groovy (build.gradle)
I dunno

</details>

<details>
<summary>Config Files</summary>

```java
package net.yourmod.mod;

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

```

**TL;DR:**
- Config creation logic is similar to SuperMartijn642's Config Lib, but isn't based on Suppliers making it editable through your own code instead of it being read-only
- MConfigBuilder takes:
    - Mod ID,
    - A file suffix/file name when subdirectory is made,
    - Config file type,
    - Boolean true/false do you want your config to be in a subfolder
- Config Entries can be Integers, Floats, Doubles, Longs, Booleans, Strings or Lists of those same
- `value = builder.define(key, ...)` defines a config key
- `value = builder.defineList(key, ...)` defines a config list key
- `builder.openCat("CategoryName")` opens a new category
    - Some config file types don't support categories (`.properties`)
- `builder.closeCat()` closes last opened category
- Categories can be put within eachother
- `.comment("example")` can be used standalone with just `builder.comment("An example");`, or combined with entries or categories, e.g. `value = builder.comment("A value").define(key, ...)`
    - Some config file types don't support comments (`.json`)

</details>

<details>
<summary>Initializing The config</summary>

```java
package net.yourmod.mod;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class YourMod implements ModInitializer {
    @Override
    public void onInitialize() {
        ExampleConfig.register(); // That's literally it
    }
}

```

</details>

**Util Library**<br>

<details>
<summary>How to Use The Util Classes</summary>

Idk how to explain all that so good luck

</details>

<hr>
