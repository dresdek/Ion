plugins {
	java
	id("org.jetbrains.kotlin.jvm") version "1.6.10"
	id("com.github.johnrengelman.shadow") version "7.1.1"
	id("io.papermc.paperweight.userdev") version "1.2.0"
}

group = "net.horizonsend"
version = "1.0"

allprojects {
	repositories {
		maven { url = uri("https://papermc.io/repo/repository/maven-public/")}
		maven { url = uri("https://nexus.scarsz.me/content/groups/public/") }
		maven { url = uri("https://repo.aikar.co/content/groups/aikar/"); content{ excludeModule("org.bukkit", "bukkit") } }
		maven { url = uri("https://www.myget.org/F/egg82-java/maven/") }
		maven { url = uri("https://maven.sk89q.com/repo/") }
		maven { url = uri("https://repo.citizensnpcs.co/") }
		maven { url = uri("https://repo.mikeprimm.com/") }
		maven { url = uri("https://jitpack.io") }
	}
}

allprojects {
	apply(plugin = "java")
	apply(plugin = "io.papermc.paperweight.userdev")
	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "com.github.johnrengelman.shadow")

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
			destinationDirectory.set(file(rootProject.projectDir.absolutePath + "/build/libs"))
		}

		shadowJar {
			destinationDirectory.set(file(rootProject.projectDir.absolutePath + "/build/jar"))
			minimize()
		}

		build {
			dependsOn(reobfJar)
		}
	}
}