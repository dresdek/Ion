package net.horizonsend.ion.proxy.commands

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.velocitypowered.api.proxy.Player
import java.util.concurrent.TimeUnit
import kotlin.random.Random
import net.horizonsend.ion.proxy.Ion.Companion.ionInstance
import net.horizonsend.ion.proxy.Ion.Companion.server
import net.kyori.adventure.text.Component.text

@CommandAlias("link")
@Suppress("unused")
object Link: BaseCommand() {
	private const val CODE_LIFETIME_SECONDS = 120L

	private val linkCodes = mutableMapOf<String, String>()

	private fun generateUniqueCode(): String {
		val linkCode = (0..3).map{ Char(Random.nextInt(65, 90)) }.joinToString()
		return if (linkCodes.containsKey(linkCode)) generateUniqueCode() else linkCode
	}

	@Default
	@Description("Link your discord account to your minecraft account.")
	fun link(source: Player) {
		val linkCode = generateUniqueCode()

		source.sendMessage(text("Your link code is: $linkCode\nMessage this code to the Horizon's End Chat Link using your discord account to link your accounts."))

		linkCodes[linkCode] = source.uniqueId.toString()

		server.scheduler.buildTask(ionInstance) {
			linkCodes.remove(linkCode)
		}.apply {
			delay(CODE_LIFETIME_SECONDS, TimeUnit.SECONDS)
			schedule()
		}
	}

	fun validateLinkCode(linkCode: String): String? = linkCodes.remove(linkCode)
}