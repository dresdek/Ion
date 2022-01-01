plugins {
	id("org.jetbrains.kotlin.jvm") version "1.6.10"
	id("com.github.johnrengelman.shadow") version "7.1.2"
}

allprojects {
	//apply plugins
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "com.github.johnrengelman.shadow")

	repositories {
		maven { url = uri("https://repo.aikar.co/content/groups/aikar/"); content{ excludeModule("org.bukkit", "bukkit") } }
		mavenCentral()
	}

	dependencies {
		implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
	}
}

tasks {
	replace("build")

	build {
		dependsOn("server:reobfJar")
	}
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}