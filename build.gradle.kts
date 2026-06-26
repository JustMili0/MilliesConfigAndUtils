import net.fabricmc.loom.api.LoomGradleExtensionAPI
import net.fabricmc.loom.api.fabricapi.FabricApiExtension
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    java
    alias(libs.plugins.arch.loom) apply false
    alias(libs.plugins.arch.plugin)
    alias(libs.plugins.shadow) apply false
}

val mcVersion = libs.versions.minecraft.get()

architectury {
    minecraft = mcVersion
}

allprojects {
    group = rootProject.property("maven_group") as String
    version = rootProject.property("mod_version") as String

    repositories {
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "dev.architectury.loom")
    apply(plugin = "architectury-plugin")

    val libs = rootProject.extensions.getByName<LibrariesForLibs>("libs")
    base { archivesName.set(rootProject.property("archives_base_name") as String) }

    repositories {
        maven("https://maven.parchmentmc.org")
        maven("https://maven.minecraftforge.net/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
        maven("https://maven.bawnorton.com/releases") // MixinSqured
        maven("https://maven.enjarai.dev/mirrors") // MixinSqured
        maven("https://maven.terraformersmc.com/") // Mod Menu
    }

    val loom = project.extensions.getByName<LoomGradleExtensionAPI>("loom")
    val fabricApi = project.extensions.getByName<FabricApiExtension>("fabricApi")
    loom.silentMojangMappingsLicense()

    dependencies {
        "minecraft"(libs.minecraft.get())

        // Uncomment if you want datagen (fabric)
        // IMPORTANT: Only for the sake of compiling - not to be used for anything else!
        "modCompileOnly"(fabricApi.module("fabric-recipe-api-v1", libs.versions.fabric.api.get()))

        "mappings"(loom.layered {
            officialMojangMappings()
            parchment("org.parchmentmc.data:parchment-${mcVersion}:${libs.versions.parchment.get()}@zip")
        })
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    tasks.withType<JavaCompile>().configureEach {
        options.release.set(17)
    }

    val detectedPlatform = when {
        project.name.contains("fabric", true) -> "Fabric"
        project.name.contains("forge", true) -> "Forge"
        else -> null
    }
    project.version = if (detectedPlatform != null) {
        "${rootProject.property("mod_version")}+mc${mcVersion}-${detectedPlatform}"
    } else {
        rootProject.property("mod_version") as String
    }

    tasks.withType<Jar>().configureEach {
        archiveBaseName.set(rootProject.property("archives_base_name") as String)
        archiveVersion.set(project.version.toString())
    }

    tasks.withType<Jar>().configureEach {
        from(rootProject.file("LICENSE"))
        from(rootProject.file("LICENSE-APACHE-2"))
    }
}