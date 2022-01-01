package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.command.CommandSource
import kotlin.time.Duration

@CommandAlias("mute")
object MuteCommand: BaseCommand() {
	@Default
	@CommandCompletion("@players")
	@CommandPermission("ion.mute")
	@Description("Mutes a player")
	fun mute(source: CommandSource, target: String, reason: String) {
		TODO()
	}

	@CommandAlias("tempmute")
	@CommandCompletion("@players")
	@CommandPermission("ion.mute")
	@Description("Mutes a player temporarily")
	fun temp(source: CommandSource, target: String, time: Duration, reason: String) {
		TODO()
	}
}