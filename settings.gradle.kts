pluginManagement {
	repositories {
		maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        maven("https://maven.minecraftforge.net")
		mavenCentral()
		gradlePluginPortal()
	}
}

rootProject.name = "Millie's Config & Utils"
