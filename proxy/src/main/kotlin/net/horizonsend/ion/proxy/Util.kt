package net.horizonsend.ion.proxy

import com.velocitypowered.api.proxy.Player
import net.horizonsend.ion.proxy.Ion.Companion.plugin

fun targetsFromIonSelector(selector: String): Set<Player> =
	if (selector == "*") plugin.server.allPlayers.toSet()
	else if (selector.startsWith("@")) {
		val targetServer = plugin.server.getServer(selector.substring(1)).orElse(null)
		targetServer?.playersConnected?.toSet() ?: emptySet()
	} else {
		val targetPlayer = plugin.server.getPlayer(selector).orElse(null)
		if (targetPlayer != null) setOf(targetPlayer) else emptySet()
	}