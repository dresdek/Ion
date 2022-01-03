package net.horizonsend.ion.server

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.NETHER_PORTAL

object Tweaks: Listener {
	@EventHandler
	fun onCreatureSpawn(event: CreatureSpawnEvent) {
		if (event.spawnReason == NETHER_PORTAL) event.isCancelled = true
	}
}