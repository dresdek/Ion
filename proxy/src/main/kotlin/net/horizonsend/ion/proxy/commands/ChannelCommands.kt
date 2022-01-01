package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.proxy.Player

@CommandAlias("channel")
object ChannelCommands: BaseCommand() {
	@Default
	@Description("Lists channels you have access to")
	fun list(source: Player) {
		TODO()
	}

	@Description("Creates a channel with the name")
	fun create(source: Player, channel: String) {
		TODO()
	}

	@Description("Deletes a channel with the name")
	fun delete(source: Player, channel: String) {
		TODO()
	}

	@Description("Gets information about a channel")
	fun info(source: Player, channel: String) {
		TODO()
	}

	@CommandAlias("c")
	@Description("Switches your active channel to a channel")
	fun switch(source: Player, channel: String) {
		TODO()
	}

	@Description("Mutes a channel")
	fun mute(source: Player, channel: String) {
		TODO()
	}

	@Description("Sends a message to a channel")
	fun send(source: Player, channel: String, message: String) {
		TODO()
	}

	@Description("Renames a channel")
	fun rename(source: Player, channel: String, newName: String) {
		TODO()
	}

	@Description("Add a user to a channel")
	fun add(source: Player, channel: String, player: String) {
		TODO()
	}

	@Description("Remove a user from a channel")
	fun remove(source: Player, channel: String, player: String) {
		TODO()
	}

	@Description("Transfer ownership of a channel")
	fun transfer(source: Player, channel: String, player: String) {
		TODO()
	}

	@Description("Set the discord mirror of a channel")
	fun discord(source: Player, channel: String, discord: Long) {
		TODO()
	}
}