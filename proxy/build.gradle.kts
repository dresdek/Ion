plugins {
	kotlin("kapt")
	kotlin("plugin.serialization") version "1.6.10"
}

repositories {
	maven("https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
	compileOnly("com.velocitypowered:velocity-api:3.1.1")

	kapt("com.velocitypowered:velocity-api:3.1.1")

	implementation("co.aikar:acf-velocity:0.5.1-SNAPSHOT")
	implementation("net.dv8tion:JDA:5.0.0-alpha.4")
}

tasks.shadowJar {
	archiveFileName.set("../../../build/IonProxy.jar")
}