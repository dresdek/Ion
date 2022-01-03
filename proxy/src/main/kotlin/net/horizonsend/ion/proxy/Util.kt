package net.horizonsend.ion.proxy

import com.velocitypowered.api.proxy.Player
import java.net.URL
import java.util.UUID
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import net.horizonsend.ion.proxy.Ion.Companion.ionInstance
import net.horizonsend.ion.proxy.data.UUIDData

fun targetsFromIonSelector(selector: String): Set<Player> =
	if (selector == "*") ionInstance.server.allPlayers.toSet()
	else if (selector.startsWith("@")) {
		val targetServer = ionInstance.server.getServer(selector.substring(1)).orElse(null)
		targetServer?.playersConnected?.toSet() ?: emptySet()
	} else {
		val targetPlayer = ionInstance.server.getPlayer(selector).orElse(null)
		if (targetPlayer != null) setOf(targetPlayer) else emptySet()
	}

// TODO: Cache these requests to avoid spamming the API
fun getUUIDFromName(name: String): UUID =
	URL("https://api.mojang.com/users/profiles/minecraft/$name").openStream().use {
		UUID.fromString(Json.decodeFromString<UUIDData>(it.readAllBytes().toString()).id)
	}