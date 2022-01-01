package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.command.CommandSource
import net.horizonsend.ion.proxy.ensuredName
import net.horizonsend.ion.proxy.proxy
import net.kyori.adventure.text.Component.text

@CommandAlias("warn")
object WarnCommand: BaseCommand() {
	@Default
	@CommandCompletion("@players")
	@CommandPermission("ion.warn")
	@Description("Warns a player")
	fun warn(source: CommandSource, target: String, reason: String) {
		val player = proxy.getPlayer(target).orElse(null)
		player.sendMessage(text("Warning issued by ${source.ensuredName}!\nReason: $reason"))
	}
}