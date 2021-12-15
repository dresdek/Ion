plugins {
	java
	id("org.jetbrains.kotlin.jvm") version "1.6.10"
	id("io.papermc.paperweight.userdev") version "1.2.0"
	id("com.github.johnrengelman.shadow") version "7.1.1"
}

group = "net.horizonsend"
version = "1.0"

repositories {
	mavenCentral() // general maven central repository
	maven { url = uri("https://jitpack.io") } // used for github projects without their own repo, com.github.User:Project:Tag
	maven { url = uri("https://papermc.io/repo/repository/maven-public/") } // Official PaperMC repository for API
	maven { url = uri("https://maven.sk89q.com/repo/") } // used for WorldEdit
	maven { url = uri("https://www.myget.org/F/egg82-java/maven/") } // used for EventChain
	maven { url = uri("https://repo.aikar.co/content/groups/aikar/"); content { excludeGroup("org.bukkit") } } // aikar's repository which mirrors lots of Minecraft things plus hosts his own projects
	maven { url = uri("https://nexus.scarsz.me/content/groups/public/") } // used for discordsrv
	maven { url = uri("https://nexus.vankka.dev/repository/maven-public/") } // used for dependency of discordsrv (MCDiscordReserializer)
	maven { url = uri("https://raw.githubusercontent.com/FabioZumbi12/UltimateChat/mvn-repo/") } // used for dependency of discordsrv (UltimateChat)
	maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") } // anvilgui repo
	maven { url = uri("https://repo.citizensnpcs.co/") } // Citizens NPCs plugin repo
	maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") }
	maven { url = uri("https://repo.mikeprimm.com/") } // Used for dynmap
}

dependencies {
	// https://papermc.io (full server hosted at https://maven.starlegacy.net/)
	compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
	paperDevBundle("1.17.1-R0.1-SNAPSHOT")

	compileOnly("net.luckperms:api:5.3")
	compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") // https://github.com/MilkBowl/Vault
	compileOnly("com.github.bloodmc:GriefDefenderAPI:master") // https://github.com/bloodmc/GriefDefender/
	compileOnly("com.discordsrv:discordsrv:1.24.0")
	compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.0-SNAPSHOT") // https://github.com/EngineHub/WorldEdit
	compileOnly("net.citizensnpcs:citizens:2.0.27-SNAPSHOT") // https://github.com/CitizensDev/Citizens2/

	// https://github.com/webbukkit/dynmap
	compileOnly("us.dynmap:dynmap-api:3.1")
	compileOnly("us.dynmap:spigot:3.1")

	implementation("com.daveanthonythomas.moshipack:moshipack:1.0.1") // https://github.com/davethomas11/MoshiPack
	implementation("org.litote.kmongo:kmongo:4.4.0") // https://github.com/Litote/kmongo
	implementation("redis.clients:jedis:3.7.1") // https://github.com/xetorthio/jedis
	implementation("io.github.config4k:config4k:0.4.2") // https://github.com/config4k/config4k
	implementation("com.googlecode.cqengine:cqengine:3.6.0") // https://github.com/npgall/cqengine
	implementation("com.github.jkcclemens:khttp:0.1.0") // https://github.com/jkcclemens/khttp
	implementation("net.wesjd:anvilgui:1.5.3-SNAPSHOT")
	implementation("com.github.stefvanschie.inventoryframework:IF:0.10.3") // https://github.com/stefvanschie/IF
	implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT") // https://github.com/aikar/commands
	implementation("ninja.egg82:event-chain-bukkit:1.0.7") // https://github.com/egg82/EventChain
	implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.0")
	implementation("org.jetbrains.kotlin:kotlin-stdlib:1.6.10")
	implementation("net.dv8tion:JDA:5.0.0-alpha.2")
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(16))
}

tasks {
	compileJava {
		options.encoding = Charsets.UTF_8.name()

		options.release.set(16)
	}

	shadowJar {
		minimize()
	}

	build {
		dependsOn(shadowJar)
		dependsOn(reobfJar)
	}
}
