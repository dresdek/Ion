plugins {
	java
	id("org.jetbrains.kotlin.jvm") version "1.6.10"
	id("io.papermc.paperweight.userdev") version "1.3.4-SNAPSHOT"
	id("com.github.johnrengelman.shadow") version "7.1.1"
}

repositories {
	maven("https://papermc.io/repo/repository/maven-public/")
	maven("https://nexus.scarsz.me/content/groups/public/")
	maven{
		uri("https://repo.aikar.co/content/groups/aikar/")
		content{ excludeModule("org.bukkit", "bukkit") }
	}
	maven("https://www.myget.org/F/egg82-java/maven/")
	maven("https://maven.sk89q.com/repo/")
	maven("https://repo.citizensnpcs.co/")
	maven("https://repo.mikeprimm.com/")
	maven("https://jitpack.io")
	mavenCentral()
}

dependencies {
	compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
	paperDevBundle("1.18.1-R0.1-SNAPSHOT")

	compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.8") // https://github.com/EngineHub/WorldEdit
	compileOnly("com.github.bloodmc:GriefDefenderAPI:master") // https://github.com/bloodmc/GriefDefender/
	compileOnly("net.citizensnpcs:citizens:2.0.27-SNAPSHOT") // https://github.com/CitizensDev/Citizens2/
	compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") // https://github.com/MilkBowl/Vault
	compileOnly("com.discordsrv:discordsrv:1.24.0")
	compileOnly("us.dynmap:dynmap-api:3.1") // https://github.com/webbukkit/dynmap
	compileOnly("net.luckperms:api:5.3")
	compileOnly("us.dynmap:spigot:3.1") // https://github.com/webbukkit/dynmap

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
	implementation("com.github.stefvanschie.inventoryframework:IF:0.10.3") // https://github.com/stefvanschie/IF
	implementation("com.daveanthonythomas.moshipack:moshipack:1.0.1") // https://github.com/davethomas11/MoshiPack
	implementation("com.googlecode.cqengine:cqengine:3.6.0") // https://github.com/npgall/cqengine
	implementation("ninja.egg82:event-chain-bukkit:1.0.7") // https://github.com/egg82/EventChain
	implementation("com.github.jkcclemens:khttp:0.1.0") // https://github.com/jkcclemens/khttp
	implementation("net.wesjd:anvilgui:1.5.3-SNAPSHOT")
	implementation("co.aikar:acf-paper:0.5.1-SNAPSHOT") // https://github.com/aikar/commands
	implementation("org.litote.kmongo:kmongo:4.4.0") // https://github.com/Litote/kmongo
	implementation("net.dv8tion:JDA:5.0.0-alpha.3")
	implementation("redis.clients:jedis:3.7.1") // https://github.com/xetorthio/jedis
}

sourceSets {
	main {
		java {
			srcDir("Ion/src/main/kotlin")
			srcDir("StarLegacy/src/main/java")
			srcDir("StarLegacy/src/main/kotlin")
		}
		resources {
			srcDir("Ion/src/main/resources")
		}
	}

}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.reobfJar {
	outputJar.set(file(rootProject.projectDir.absolutePath + "/build/Ion.jar"))
}