package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.command.CommandSource
import net.horizonsend.ion.proxy.Ion.Companion.ionInstance
import net.horizonsend.ion.proxy.targetsFromIonSelector
import net.kyori.adventure.text.Component.text

@CommandAlias("move")
@Suppress("unused")
object Move: BaseCommand() {
	@Default
	@CommandCompletion("@multiTargets @servers")
	@CommandPermission("ion.move")
	@Description("Move a player to a server")
	fun move(source: CommandSource, target: String, server: String) {
		val targetServer = ionInstance.server.getServer(server).orElse(null)

		if (targetServer == null) {
			source.sendMessage(text("Target Server does not exist."))
			return
		}

		targetsFromIonSelector(target).forEach {
			it.createConnectionRequest(targetServer).connect()
		}
	}
}