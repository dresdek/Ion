package net.horizonsend.ion.proxy.database.data

import org.bson.codecs.pojo.annotations.BsonId

data class CrossAccountData(
	@BsonId val _id: Int, // CrossAccountData ID
)