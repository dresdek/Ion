package net.horizonsend.ion.proxy

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import java.util.UUID

val CommandSource.ensuredName: String get() =
	when (this) {
		is Player -> this.username
		else -> "Server"
	}

val CommandSource.ensuredUUID: UUID get() =
	when (this) {
		is Player -> this.uniqueId
		else -> UUID.fromString("00000000-0000-0000-0000-000000000000")
	}