package net.horizonsend.ion.proxy

data class Config(
	val host: String = "localhost",
	val username: String = "test",
	val password: String = "test",
	val database: String = "test",
	val port: Int = 27017
)
