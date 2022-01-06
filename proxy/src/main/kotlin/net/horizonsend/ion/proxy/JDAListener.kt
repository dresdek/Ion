package net.horizonsend.ion.proxy

import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.horizonsend.ion.proxy.commands.Link.validateLinkCode
import net.horizonsend.ion.proxy.database.MongoManager.getAccountData
import net.horizonsend.ion.proxy.database.MongoManager.saveAccountData

object JDAListener: ListenerAdapter() {
	override fun onMessageReceived(event: MessageReceivedEvent) {
		if (event.isFromType(ChannelType.PRIVATE)) {
			if (event.message.contentRaw.length == 4) {
				val player = validateLinkCode(event.message.contentRaw)

				if (player != null) {
					saveAccountData(getAccountData(player)?.apply { discordUserId = event.author.id } ?: throw IllegalStateException("Account data not found"))

				} else event.message.reply("Link code is invalid, non-existent, or expired.")
			} else event.message.reply("Message is too short or too long to be a verification code. Please resend the code.")
		}
	}
}