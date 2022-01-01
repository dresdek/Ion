package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.command.CommandSource
import java.io.File
import kotlinx.serialization.ExperimentalSerializationApi
import net.horizonsend.ion.proxy.banDataDirectory
import net.horizonsend.ion.proxy.getUUIDFromName

@CommandAlias("unban")
object UnbanCommand: BaseCommand() {
	@ExperimentalSerializationApi
	@Default
	@CommandPermission("ion.unban")
	@Description("Unbans a player")
	fun unban(source: CommandSource, target: String) {
		File(banDataDirectory, getUUIDFromName(target).toString() + ".json").delete()
	}
}