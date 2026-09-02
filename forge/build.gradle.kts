import multiloader.*

plugins {
    id("com.gradleup.shadow")
    alias(libs.plugins.publish)
}

val generatedResources = file("src/generated")

sourceSets {
    main {
        resources.srcDir(generatedResources)
    }
}

loom {
    forge {
        mixinConfig(
            "${modId}.mixins.json",
            "${modId}-forge.mixins.json"
        )
    }

    mixin.useLegacyMixinAp = true

    runs {
        create("data") {
            data()
            programArgs("--all", "--mod", modId)
            programArgs("--output", generatedResources.absolutePath)
        }
    }
}

architectury {
    platformSetupLoomIde()
    forge()
}

configurations {
    val common by creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
    named("compileClasspath") { extendsFrom(common) }
    named("runtimeClasspath") { extendsFrom(common) }
    named("developmentForge") { extendsFrom(common) }

    val shadowBundle by creating {
        isCanBeResolved = true
        isCanBeConsumed = false
    }
}

dependencies {
    forge(libs.forge.get())
    modImplementation(libs.forge.get())

    // Forge dependencies go here

    // Forge doesn't mainline MixinExtras until 1.21.11, so here we need it
    compileOnly(annotationProcessor("io.github.llamalad7:mixinextras-common:${libs.versions.mixinextras.get()}")!!)
    implementation(include("io.github.llamalad7:mixinextras-forge:${libs.versions.mixinextras.get()}")!!)
    //compileOnly(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-common:${libs.versions.mixinsquared.get()}")!!)
    //implementation(include("com.github.bawnorton.mixinsquared:mixinsquared-forge:${libs.versions.mixinsquared.get()}")!!)

    "common"(project(":common", "namedElements")) { isTransitive = false }
    "shadowBundle"(project(":common", "transformProductionForge"))
}

tasks.processResources {
    filesMatching("META-INF/mods.toml") {
        expand(
            "mod_id" to modId,
            "mod_name" to modName,
            "mod_version" to modVersion,
            "mod_description" to modDesc,
            "mod_authors" to modAuthor,
            "mod_license" to modLicense,
            "forge_version" to libs.versions.forge.asProvider().get(),
            "minecraft_version_constraint" to root.property("minecraft_version_constraint_forge")
        )
    }
}

tasks.shadowJar {
    configurations = listOf(project.configurations["shadowBundle"])
    archiveClassifier.set("dev-shadow")
}

tasks.remapJar {
    inputFile.set(tasks.shadowJar.get().archiveFile)
}

publishMods {
    file = tasks.remapJar.get().archiveFile
    modLoaders.add("forge")

    changelog = readChangelogFromBranch("origin/rep-info", ".Changelogs/${modVersion}-Changelog.md")

    modrinth {
        accessToken = property("modrinth_token") as String
        projectId = "j6khQEIz"

        minecraftVersions.add(mcVersion)
        environment = CLIENT_AND_SERVER
        // STABLE, BETA, ALPHA
        type = STABLE
    }

//    curseforge {
//        accessToken = property("curseforge_token") as String
//        projectId = "abcdefg"
//
//        minecraftVersions.add(mcVersion)
//        client = true
//        server = true
//        // STABLE, BETA, ALPHA
//        type = STABLE
//    }
}