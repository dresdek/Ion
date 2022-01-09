plugins {
	java
	kotlin("jvm") version "1.6.10"
	id("com.github.johnrengelman.shadow") version "7.1.2"
}

allprojects {
	apply(plugin = "kotlin")
	apply(plugin = "com.github.johnrengelman.shadow")

	repositories {
		maven { url = uri("https://repo.aikar.co/content/groups/aikar/"); content{
			excludeModule("org.bukkit", "bukkit")
			excludeModule("net.wesjd", "anvilgui")
		}}
		mavenCentral()
	}

	dependencies {
		implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
		implementation("org.litote.kmongo:kmongo:4.4.0") // https://github.com/Litote/kmongo
	}

	tasks {
		compileJava {
			options.compilerArgs.add("-parameters")
			options.isFork = true
		}

		compileKotlin {
			kotlinOptions.javaParameters = true
		}
	}

	java {
		toolchain.languageVersion.set(JavaLanguageVersion.of(17))
	}
}

tasks {
	create("ionBuildServer") {
		dependsOn("server:reobfJar")
	}

	create("ionBuildProxy") {
		dependsOn("proxy:shadowJar")
	}

	create("ionBuild") {
		dependsOn("ionBuildServer", "ionBuildProxy")
	}
}