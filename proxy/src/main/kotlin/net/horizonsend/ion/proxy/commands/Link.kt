package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.command.CommandSource
import kotlin.random.Random
import net.kyori.adventure.text.Component.text

@CommandAlias("link")
@Suppress("unused")
object Link: BaseCommand() {
	@Default
	@Description("Link your discord account to your minecraft account.")
	fun link(source: CommandSource) {
		val linkCode = (0..3).map{ Char(Random.nextInt(65, 90)) }.joinToString()

		source.sendMessage(text("Your link code is: $linkCode\nMessage this code to the Horizon's End Chat Link using your discord account to link your accounts."))
	}
}