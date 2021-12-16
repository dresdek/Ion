plugins {
	java
	id("org.jetbrains.kotlin.jvm") version "1.6.10"
	id("com.github.johnrengelman.shadow") version "7.1.1"
	id("io.papermc.paperweight.userdev") version "1.2.0"
}

group = "net.horizonsend"
version = "1.0"

allprojects {
	apply(plugin = "java")
	apply(plugin = "io.papermc.paperweight.userdev")
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "com.github.johnrengelman.shadow")

	repositories {
		maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
	}

	dependencies {
		compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
		paperDevBundle("1.17.1-R0.1-SNAPSHOT")
	}

	java {
		toolchain.languageVersion.set(JavaLanguageVersion.of(16))
	}

	tasks {
		compileJava {
			options.encoding = Charsets.UTF_8.name()

			options.release.set(16)
		}

		jar {
			destinationDir = file(rootProject.projectDir.absolutePath + "/build/libs")
		}

		shadowJar {
			destinationDir = file(rootProject.projectDir.absolutePath + "/build/jar")
			minimize()
		}

		build {
			dependsOn(reobfJar)
		}
	}
}