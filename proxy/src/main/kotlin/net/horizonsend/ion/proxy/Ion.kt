package net.horizonsend.ion.proxy

import co.aikar.commands.VelocityCommandManager
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import net.horizonsend.ion.proxy.commands.MoveCommand
import net.horizonsend.ion.proxy.commands.SwitchCommand
import org.slf4j.Logger

@Plugin(id = "ion", name = "Ion (Proxy)", version = "1.0.0", description = "Ion (Proxy)", authors = ["PeterCrawley"], url = "https://horizonsend.net")
class Ion @Inject constructor(val server: ProxyServer, logger: Logger) {
	companion object {
		lateinit var plugin: Ion
			private set
	}

	init { plugin = this }

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