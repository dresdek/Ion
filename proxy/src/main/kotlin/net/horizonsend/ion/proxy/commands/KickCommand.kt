package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.command.CommandSource
import net.horizonsend.ion.proxy.ensuredName
import net.horizonsend.ion.proxy.targetsFromIonSelector
import net.kyori.adventure.text.Component.text

@CommandAlias("kick")
object KickCommand: BaseCommand() {
	@Default
	@CommandCompletion("@multiTargets")
	@CommandPermission("ion.kick")
	@Description("Kick a player from the server")
	fun kick(sender: CommandSource, target: String, reason: String) {
		targetsFromIonSelector(target).forEach {
			it.disconnect(text("You were kicked by ${sender.ensuredName}!\n\nReason: $reason"))
		}
	}
}