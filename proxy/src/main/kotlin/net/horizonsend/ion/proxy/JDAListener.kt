package net.horizonsend.ion.proxy

import net.dv8tion.jda.api.entities.ChannelType
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.horizonsend.ion.proxy.Ion.Companion.server
import net.horizonsend.ion.proxy.commands.Link.validateLinkCode
import net.horizonsend.ion.proxy.database.MongoManager.getAccountData
import net.horizonsend.ion.proxy.database.MongoManager.saveAccountData
import net.kyori.adventure.text.Component.text

object JDAListener: ListenerAdapter() {
	override fun onMessageReceived(event: MessageReceivedEvent) {
		if (event.isFromType(ChannelType.PRIVATE)) {
			if (event.message.contentRaw.length == 4) {
				val player = validateLinkCode(event.message.contentRaw)

				if (player != null) {
					saveAccountData(getAccountData(player).apply { discordUserId = event.author.id })

					event.message.reply("Your discord account has been linked to the minecraft account \"${nameFromUUID(player)}\". If this is a mistake please contact Horizon's End staff.")

					server.getPlayer(player).ifPresent{
						it.sendMessage(text("Your minecraft account has been linked to the discord account \"${event.author.asTag}\". If this is a mistake please contact Horizon's End staff."))
					}

				} else event.message.reply("Link code is invalid, non-existent, or expired.")
			} else event.message.reply("Message is too short or too long to be a verification code. Please resend the code.")
		}
	}
}