package net.horizonsend.ion.proxy

import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.horizonsend.ion.proxy.Ion.Companion.server
import net.horizonsend.ion.proxy.commands.Link.validateLinkCode
import net.horizonsend.ion.proxy.database.MongoManager.getAccountData
import net.horizonsend.ion.proxy.database.MongoManager.saveAccountData
import net.kyori.adventure.text.Component

object JDAListener: ListenerAdapter() {
	override fun onMessageReceived(event: MessageReceivedEvent) {
		if (!event.isFromType(ChannelType.PRIVATE)) return
		if (event.author.isBot) return

		if (event.message.contentRaw.length != 4) {
			event.message.reply("Message is too short or too long to be a verification code. Please resend the code.").queue()
			return
		}

		val player = validateLinkCode(event.message.contentRaw)

		if (player == null) {
			event.message.reply("Link code is invalid, non-existent, or expired.").queue()
			return
		}

		getAccountData(player).apply {
			if (discordUserId == null) {
				event.message.reply("Your discord account has already been linked to a minecraft account. Accounts can only be unlinked by Horizon's End staff.").queue()
				return
			}

			discordUserId = event.author.id
			saveAccountData(this)

			event.message.reply("Your discord account has been linked to the minecraft account \"${nameFromUUID(player)}\". If this is a mistake please contact Horizon's End staff.").queue()

			server.getPlayer(player).ifPresent {
				it.sendMessage(Component.text("Your minecraft account has been linked to the discord account \"${event.author.asTag}\". If this is a mistake please contact Horizon's End staff."))
			}
		}
	}
}