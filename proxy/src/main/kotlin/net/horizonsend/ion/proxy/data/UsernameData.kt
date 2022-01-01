package net.horizonsend.ion.proxy.data

import kotlinx.serialization.Serializable

@Serializable
data class UsernameData(
	val name: String,
	val changedToAt: Long = 0
)
