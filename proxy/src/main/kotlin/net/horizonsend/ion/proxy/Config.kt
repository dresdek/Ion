package net.horizonsend.ion.proxy

import kotlinx.serialization.Serializable

@Serializable
data class Config(
	val host: String = "localhost",
	val username: String = "test",
	val password: String = "test",
	val database: String = "test",
	val port: Int = 27017,

	val discordToken: String = "",
)
