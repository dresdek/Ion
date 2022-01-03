package net.horizonsend.ion.proxy.database.data

import org.bson.codecs.pojo.annotations.BsonId

data class AccountData(
	@BsonId val _id: String, // Minecraft UUID
	val crossAccountDataId: Int // CrossAccountData ID
)