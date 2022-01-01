package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.command.CommandSource

@CommandAlias("unmute")
object UnmuteCommand: BaseCommand() {
	@Default
	@CommandCompletion("@players")
	@CommandPermission("ion.unmute")
	@Description("Unmute a player")
	fun unmute(source: CommandSource, target: String) {
		TODO()
	}
}