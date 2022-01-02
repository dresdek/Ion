package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.command.CommandSource
import kotlin.time.Duration
import net.horizonsend.ion.proxy.banPlayer
import net.horizonsend.ion.proxy.ensuredUUID
import net.horizonsend.ion.proxy.getUUIDFromName
import net.horizonsend.ion.proxy.proxy

@CommandAlias("ban")
object BanCommand: BaseCommand() {
	@Default
	@CommandCompletion("@players")
	@CommandPermission("ion.ban")
	@Description("Ban a player")
	fun ban(source: CommandSource, target: String, reason: String) {
		val playerUUID = proxy.getPlayer(target).orElse(null)?.ensuredUUID ?: getUUIDFromName(target)

		banPlayer(source, playerUUID, reason)
	}

	@CommandCompletion("@players")
	@CommandPermission("ion.ban")
	@CommandAlias("tempban")
	@Description("Ban a player temporarily")
	fun temp(source: CommandSource, target: String, duration: Duration, reason: String) {
		TODO()
	}
}