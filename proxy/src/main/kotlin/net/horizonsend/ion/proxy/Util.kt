package net.horizonsend.ion.proxy

import com.velocitypowered.api.proxy.Player
import net.horizonsend.ion.proxy.Ion.Companion.ionInstance

fun targetsFromIonSelector(selector: String): Set<Player> =
	if (selector == "*") ionInstance.server.allPlayers.toSet()
	else if (selector.startsWith("@")) {
		val targetServer = ionInstance.server.getServer(selector.substring(1)).orElse(null)
		targetServer?.playersConnected?.toSet() ?: emptySet()
	} else {
		val targetPlayer = ionInstance.server.getPlayer(selector).orElse(null)
		if (targetPlayer != null) setOf(targetPlayer) else emptySet()
	}