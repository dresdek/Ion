package net.horizonsend.ion.proxy

import co.aikar.commands.VelocityCommandManager
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import java.io.File
import java.nio.file.Path
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.horizonsend.ion.proxy.commands.BanCommand
import net.horizonsend.ion.proxy.commands.KickCommand
import net.horizonsend.ion.proxy.commands.MoveCommand
import net.horizonsend.ion.proxy.commands.SwitchCommand
import net.horizonsend.ion.proxy.commands.UnbanCommand
import net.horizonsend.ion.proxy.commands.WarnCommand
import net.horizonsend.ion.proxy.data.BanData
import org.slf4j.Logger

@Plugin(id = "ion", name = "Ion", version = "2.0.0", description = "Ion Proxy Plugin", authors = ["PeterCrawley"], url = "https://horizonsend.net")
class IonProxyPlugin @Inject constructor(val server: ProxyServer, logger: Logger, @DataDirectory val dataDirectory: Path) {
	companion object {
		lateinit var plugin: IonProxyPlugin
			private set
	}

	init { plugin = this }

	@Subscribe
	fun onStart(event: ProxyInitializeEvent) {
		VelocityCommandManager(server, this).apply {
			setOf(BanCommand, KickCommand, MoveCommand, SwitchCommand, UnbanCommand, WarnCommand).forEach { registerCommand(it) }

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

			@Suppress("DEPRECATION")
			enableUnstableAPI("help")
		}
	}

	@ExperimentalSerializationApi
	@Subscribe
	fun onLogin(event: LoginEvent) {
		val banDataFile = File(banDataDirectory, "${event.player.ensuredUUID}.json")

		if (banDataFile.exists()) {
			val banData = Json.decodeFromString<BanData>(banDataFile.readText())

			event.player.disconnect(constructBanMessage(getNameFromUUIDString(banData.issuedBy), banData.reason, banData.expires))
		}
	}
}