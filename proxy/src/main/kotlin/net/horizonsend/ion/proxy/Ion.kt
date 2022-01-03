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
import net.horizonsend.ion.proxy.commands.MoveCommand
import net.horizonsend.ion.proxy.commands.SwitchCommand
import net.horizonsend.ion.proxy.database.MongoManager
import org.slf4j.Logger

@Plugin(id = "ion", name = "Ion (Proxy)", version = "1.0.0", description = "Ion (Proxy)", authors = ["PeterCrawley"], url = "https://horizonsend.net")
class Ion @Inject constructor(val server: ProxyServer, val logger: Logger, @DataDirectory val dataDirectory: Path) {
	companion object {
		lateinit var ionInstance: Ion
			private set

		lateinit var ionConfig: Config
			private set
	}

	init {
		ionInstance = this

		val configPath = dataDirectory.resolve("config.json")

		dataDirectory.createDirectories() // Ensure the directories exist

		if (!configPath.exists()) {
			logger.warn("Failed to find the config file, creating a new one.")
			configPath.writeText(Json.encodeToString(Config()))
		}

		ionConfig = Json.decodeFromString(configPath.readText())

		MongoManager
	}

	@Subscribe
	fun onStart(event: ProxyInitializeEvent) {
		VelocityCommandManager(server, this).apply {
			setOf(MoveCommand, SwitchCommand).forEach { registerCommand(it) }

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