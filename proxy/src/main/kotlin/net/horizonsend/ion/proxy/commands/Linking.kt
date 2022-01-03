package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.proxy.Player
import kotlin.random.Random
import net.horizonsend.ion.proxy.Ion.Companion.ionInstance
import net.horizonsend.ion.proxy.getProfileFromName
import net.kyori.adventure.text.Component.text

@CommandAlias("link")
object Linking: BaseCommand() {
	private data class LinkCode(val sourceUUID: String, val targetUUID: String, val code: String, val expires: Long)

	private val linkCodes = mutableSetOf<LinkCode>()

	@CommandCompletion("@players")
	@CommandAlias("new")
	@Description("Generates a link code for linking that player to you.")
	@Suppress("unused")
	fun new(source: Player, target: String) {
		val sourceUUID = source.uniqueId.toString()

		val targetPlayer = ionInstance.server.getPlayer(target).orElse(null)
		val targetName: String
		val targetUUID: String

		if (targetPlayer == null) {
			val profile = getProfileFromName(target)
			targetName = profile.name
			targetUUID = profile.getUUID().toString()

		} else {
			targetName = targetPlayer.username
			targetUUID = targetPlayer.uniqueId.toString()
		}

		// 34 = A
		// 59 = Z
		val code = (0..3).map { Char(Random.nextInt(34, 59)) }.joinToString("")

		linkCodes.add(LinkCode(sourceUUID, targetUUID, code, System.currentTimeMillis() + 300000)) // 1000 * 60 * 5 = 300000 ( 5 minutes )

		source.sendMessage(text("Run /link $code on $target to link this account (${source.username}) to $targetName. This link code will expire in 5 minutes."))
	}
}