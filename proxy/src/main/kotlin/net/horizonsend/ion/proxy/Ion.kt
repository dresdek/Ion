package net.horizonsend.ion.proxy

import co.aikar.commands.VelocityCommandManager
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.requests.GatewayIntent.DIRECT_MESSAGES
import net.dv8tion.jda.api.utils.cache.CacheFlag.ACTIVITY
import net.dv8tion.jda.api.utils.cache.CacheFlag.CLIENT_STATUS
import net.dv8tion.jda.api.utils.cache.CacheFlag.EMOTE
import net.dv8tion.jda.api.utils.cache.CacheFlag.ONLINE_STATUS
import net.dv8tion.jda.api.utils.cache.CacheFlag.VOICE_STATE
import net.horizonsend.ion.proxy.commands.Link
import net.horizonsend.ion.proxy.commands.Move
import net.horizonsend.ion.proxy.commands.Switch
import net.horizonsend.ion.proxy.commands.Unlink
import net.horizonsend.ion.proxy.database.MongoManager
import org.slf4j.Logger

@Plugin(id = "ion", name = "Ion (Proxy)", version = "1.0.0", description = "Ion (Proxy)", authors = ["PeterCrawley"], url = "https://horizonsend.net")
class Ion @Inject constructor(val server: ProxyServer, logger: Logger, @DataDirectory val dataDirectory: Path) {
	companion object {
		lateinit var ionInstance: Ion
			private set

		val server get() = ionInstance.server

		lateinit var ionConfig: Config
			private set

		lateinit var jda: JDA
			private set
	}

	init {
		ionInstance = this

		// Loading of config
		val configPath = dataDirectory.resolve("config.json")

		dataDirectory.createDirectories() // Ensure the directories exist

		if (!configPath.exists()) {
			logger.warn("Failed to find the config file, creating a new one.")
			configPath.writeText(Json.encodeToString(Config()))
		}

		ionConfig = Json.decodeFromString(configPath.readText())

		// Connect to discord
		jda = JDABuilder.create(ionConfig.discordToken, DIRECT_MESSAGES).apply {
			disableCache(ACTIVITY, VOICE_STATE, EMOTE, CLIENT_STATUS, ONLINE_STATUS)

		}.build().apply {
			addEventListener(JDAListener)

		}

		// Init MongoDB
		MongoManager
	}

	@Subscribe
	@Suppress("UNUSED_PARAMETER") // Parameter is required to indicate what event to subscribe to
	fun onStart(event: ProxyInitializeEvent) {
		VelocityCommandManager(server, this).apply {
			setOf(Link, Move, Switch, Unlink).forEach { registerCommand(it) }

			commandCompletions.apply {
				registerCompletion("multiTargets") {
					server.allPlayers.map { it.username }.toMutableList().apply {
						add("*")
						addAll(server.allServers.map { "@${it.serverInfo.name}" })
					}
				}

				registerCompletion("players") {
					server.allPlayers.map { it.username }.toMutableList()
				}

				registerCompletion("servers") { context ->
					server.allServers.map { it.serverInfo.name }.filter { context.sender.hasPermission("ion.server.$it") }
				}
			}

			@Suppress("DEPRECATION") // To quote Micle (Regions.kt L209) "our standards are very low"
			enableUnstableAPI("help")
		}
	}
}