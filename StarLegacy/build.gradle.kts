group = "net.starlegacy"
version = "1.0"

dependencies {
	compileOnly("net.luckperms:api:5.3")
	compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") // https://github.com/MilkBowl/Vault
	compileOnly("com.github.bloodmc:GriefDefenderAPI:master") // https://github.com/bloodmc/GriefDefender/
	compileOnly("com.discordsrv:discordsrv:1.24.0")
	compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.0-SNAPSHOT") // https://github.com/EngineHub/WorldEdit
	compileOnly("net.citizensnpcs:citizens:2.0.27-SNAPSHOT") // https://github.com/CitizensDev/Citizens2/

	// https://github.com/webbukkit/dynmap
	compileOnly("us.dynmap:dynmap-api:3.1")
	compileOnly("us.dynmap:spigot:3.1")

	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
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