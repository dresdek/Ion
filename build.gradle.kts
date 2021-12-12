import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    id("org.jetbrains.kotlin.jvm") version "1.5.0"
    id("com.github.johnrengelman.shadow") version "6.0.0"
}

group = "net.starlegacy"
version = "SNAPSHOT"

repositories {
    maven { url = uri("https://jitpack.io") } // used for github projects without their own repo, com.github.User:Project:Tag
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") } // Official PaperMC repository for API
    maven { url = uri("https://maven.sk89q.com/repo/") } // used for WorldEdit and WorldGuard
    maven { url = uri("https://www.myget.org/F/egg82-java/maven/") } // used for EventChain
    maven { url = uri("https://repo.aikar.co/content/groups/aikar/"); content { excludeGroup("org.bukkit") } } // aikar's repository which mirrors lots of Minecraft things plus hosts his own projects
    maven { url = uri("https://nexus.scarsz.me/content/groups/public/") } // used for discordsrv
    maven { url = uri("https://nexus.vankka.dev/repository/maven-public/") } // used for dependency of discordsrv (MCDiscordReserializer)
    maven { url = uri("https://raw.githubusercontent.com/FabioZumbi12/UltimateChat/mvn-repo/") } // used for dependency of discordsrv (UltimateChat)
    maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") } // anvilgui repo
    maven { url = uri("https://repo.citizensnpcs.co/") } // Citizens NPCs plugin repo
    maven { url = uri("https://repo.codemc.io/repository/maven-snapshots/") }
    maven { url = uri("https://maven.starlegacy.net/") } // private repository to host server binaries
    maven { url = uri("https://repo.mikeprimm.com/"); content { includeGroup("org.bukkit") } } // Used for dynmap
    mavenCentral() // general maven central repository
}

dependencies {
    // https://papermc.io (full server hosted at https://maven.starlegacy.net/)
    compileOnly("com.destroystokyo.paper:paper-api:1.16.4-R0.1-SNAPSHOT")
    compileOnly("com.destroystokyo.paper:paper:1.16.4-R0.1-SNAPSHOT")

    compileOnly("net.luckperms:api:5.0")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") // https://github.com/MilkBowl/Vault
    compileOnly("com.github.bloodmc:GriefDefenderAPI:master") // https://github.com/bloodmc/GriefDefender/
    compileOnly("com.discordsrv:discordsrv:1.20.0")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.0-SNAPSHOT") // https://github.com/EngineHub/WorldEdit
    compileOnly("net.citizensnpcs:citizens:2.0.27-SNAPSHOT") // https://github.com/CitizensDev/Citizens2/

    // https://github.com/webbukkit/dynmap
    compileOnly("us.dynmap:dynmap-api:3.1")
    compileOnly("us.dynmap:spigot:3.1")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8") // https://kotlinlang.org/
    implementation("com.daveanthonythomas.moshipack:moshipack:1.0.0-beta") // https://github.com/davethomas11/MoshiPack
    implementation("org.litote.kmongo:kmongo:4.2.3") // https://github.com/Litote/kmongo
    implementation("redis.clients:jedis:2.9.0") // https://github.com/xetorthio/jedis
    implementation("io.github.config4k:config4k:0.4.1") // https://github.com/config4k/config4k
    implementation("com.googlecode.cqengine:cqengine:3.0.0") // https://github.com/npgall/cqengine
    implementation("com.github.jkcclemens:khttp:0.1.0") // https://github.com/jkcclemens/khttp
    implementation("net.wesjd:anvilgui:1.5.0-SNAPSHOT")
    implementation("com.github.stefvanschie.inventoryframework:IF:0.5.8") // https://github.com/stefvanschie/IF
    implementation("co.aikar:acf-paper:0.5.0-SNAPSHOT") // https://github.com/aikar/commands
    implementation("ninja.egg82:event-chain-bukkit:1.0.7") // https://github.com/egg82/EventChain
    implementation("org.ejml:ejml-all:0.40")
    implementation("club.minnced:discord-webhooks:0.5.6") // https://github.com/MinnDevelopment/discord-webhooks
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.0")
    implementation("net.dv8tion:JDA:5.0.0-alpha.2")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.plusAssign("-parameters")
    options.isFork = true
    options.forkOptions.executable = "javac"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.javaParameters = true
    kotlinOptions.jvmTarget = "1.8"
}

tasks.shadowJar {
    relocate("com.fasterxml.jackson", "net.starlegacy.libs.jackson")
    relocate("co.aikar.commands", "net.starlegacy.libs.acf")
    relocate("org.ejml", "net.starlegacy.libs.ejml")
}