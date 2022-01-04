package net.horizonsend.ion.proxy.database.data

import org.bson.codecs.pojo.annotations.BsonId

data class AccountData(
	@BsonId val _id: String, // Minecraft UUID
	val discordUserId: String? = null // Discord User ID
)