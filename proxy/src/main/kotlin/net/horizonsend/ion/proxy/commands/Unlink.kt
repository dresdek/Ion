package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.command.CommandSource
import net.horizonsend.ion.proxy.database.MongoManager.getAccountData
import net.horizonsend.ion.proxy.database.MongoManager.saveAccountData
import net.horizonsend.ion.proxy.uuidFromName
import net.kyori.adventure.text.Component.text

@CommandAlias("unlink")
@Suppress("unused") // Functions are used as entry points for commands
object Unlink: BaseCommand() {
	@Default
	@CommandPermission("ion.admin")
	@CommandCompletion("@players")
	@Description("Forcibly unlink a minecraft account from a discord account")
	fun unlink(sender: CommandSource, targetUser: String) {
		sender.sendMessage(text("$targetUser has been unlinked, assuming the account exists, and was linked."))

		try { saveAccountData(getAccountData(uuidFromName(targetUser)).apply { discordUserId = null }) }
		catch (_: Exception) {} // Silently fail: This probably should not be done.
	}
}