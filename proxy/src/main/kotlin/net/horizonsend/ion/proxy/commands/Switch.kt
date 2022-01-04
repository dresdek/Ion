package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.proxy.Player
import net.horizonsend.ion.proxy.Ion.Companion.ionInstance
import net.kyori.adventure.text.Component.text

@CommandAlias("switch")
object Switch: BaseCommand() {
	@Default
	@CommandCompletion("@servers")
	@Description("Switch to a server")
	@Suppress("unused")
	fun switch(source: Player, server: String) {
		val targetServer = ionInstance.server.getServer(server).orElse(null)

		if (targetServer == null || !source.hasPermission("ion.server.$server")) {
			source.sendMessage(text("Server $server does not exist, or you can not access it."))
			return
		}

		source.createConnectionRequest(targetServer).connect()
	}
}