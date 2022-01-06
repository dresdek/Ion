package net.horizonsend.ion.proxy

import com.velocitypowered.api.proxy.Player
import java.net.URL
import java.util.UUID
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.horizonsend.ion.proxy.Ion.Companion.ionInstance
import net.horizonsend.ion.proxy.data.UsernameData

fun targetsFromIonSelector(selector: String): Collection<Player> =
	if (selector == "*") ionInstance.server.allPlayers // If * return all players
	else if (selector.startsWith("@")) { // If @ return all players in the specified server (or none if the server doesn't exist)
		ionInstance.server.getServer(selector.substring(1)).orElse(null).playersConnected ?: emptySet()
	} else { // Return the player (or none if the player doesn't exist)
		val targetPlayer = ionInstance.server.getPlayer(selector).orElse(null)
		if (targetPlayer != null) setOf(targetPlayer) else emptySet()
	}

fun nameFromUUID(uuid: UUID): String =
	URL("https://api.mojang.com/user/profiles/$uuid/names").openStream().use {
		Json.decodeFromString<List<UsernameData>>(it.readAllBytes().toString()).last().name
	}