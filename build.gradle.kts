plugins {
	java
	id("org.jetbrains.kotlin.jvm") version "1.6.10"
	id("io.papermc.paperweight.userdev") version "1.2.0"
	id("com.github.johnrengelman.shadow") version "7.1.1"
}

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

dependencies {
	compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
	paperDevBundle("1.17.1-R0.1-SNAPSHOT")

	compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.0-SNAPSHOT") // https://github.com/EngineHub/WorldEdit
	compileOnly("com.github.bloodmc:GriefDefenderAPI:master") // https://github.com/bloodmc/GriefDefender/
	compileOnly("net.citizensnpcs:citizens:2.0.27-SNAPSHOT") // https://github.com/CitizensDev/Citizens2/
	compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") // https://github.com/MilkBowl/Vault
	compileOnly("com.discordsrv:discordsrv:1.24.0")
	compileOnly("us.dynmap:dynmap-api:3.1") // https://github.com/webbukkit/dynmap
	compileOnly("net.luckperms:api:5.3")
	compileOnly("us.dynmap:spigot:3.1") // https://github.com/webbukkit/dynmap

	implementation("com.github.stefvanschie.inventoryframework:IF:0.10.3") // https://github.com/stefvanschie/IF
	implementation("com.daveanthonythomas.moshipack:moshipack:1.0.1") // https://github.com/davethomas11/MoshiPack
	implementation("com.googlecode.cqengine:cqengine:3.6.0") // https://github.com/npgall/cqengine
	implementation("ninja.egg82:event-chain-bukkit:1.0.7") // https://github.com/egg82/EventChain
	implementation("com.github.jkcclemens:khttp:0.1.0") // https://github.com/jkcclemens/khttp
	implementation("io.github.config4k:config4k:0.4.2") // https://github.com/config4k/config4k
	implementation("net.wesjd:anvilgui:1.5.3-SNAPSHOT")
	implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT") // https://github.com/aikar/commands
	implementation("org.litote.kmongo:kmongo:4.4.0") // https://github.com/Litote/kmongo
	implementation("net.dv8tion:JDA:5.0.0-alpha.2")
	implementation("redis.clients:jedis:3.7.1") // https://github.com/xetorthio/jedis
}

sourceSets {
	main {
		java {
			srcDir("Ion/src/main/java")
			srcDir("Ion/src/main/kotlin")
			srcDir("StarLegacy/src/main/java")
			srcDir("StarLegacy/src/main/kotlin")
		}
	}
	resources {
		srcDir("Ion/src/main/resources")
	}
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(16))
}

tasks {
	shadowJar {
		destinationDirectory.set(file(rootProject.projectDir.absolutePath + "/build"))
		archiveFileName.set("Ion.jar")
	}

	build {
		dependsOn(reobfJar)
		dependsOn(shadowJar)
	}
}