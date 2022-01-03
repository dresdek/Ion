package net.horizonsend.ion.proxy.data

import kotlinx.serialization.Serializable

@Serializable
data class UUIDData (
	val name: String,
	val id: String,
	val legacy: Boolean = false,
	val demo: Boolean = false
)