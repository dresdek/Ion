package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.proxy.Player
import net.horizonsend.ion.proxy.Ion.Companion.server
import net.kyori.adventure.text.Component.text

@CommandAlias("server")
@Suppress("unused") // Functions are used as entry points for commands
object Server: BaseCommand() {
	@Default
	@CommandCompletion("@servers")
	@Description("Switch to a server")
	fun switch(source: Player, targetServer: String) {
		val server = server.getServer(targetServer).orElse(null)

		if (server == null || !source.hasPermission("ion.server.$server")) {
			source.sendMessage(text("Server $server does not exist, or you can not access it."))
			return
		}

		source.createConnectionRequest(server).connect()
	}
}