package net.horizonsend.ion.proxy.data

import kotlinx.serialization.Serializable

@Serializable
data class BanData(
	val issuedBy: String,
	val reason: String,
	val expires: Long
)
