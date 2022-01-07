package net.horizonsend.ion.server.listeners.paper

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.NETHER_PORTAL
import org.bukkit.event.world.ChunkUnloadEvent

object Tweaks: Listener {
	@EventHandler
	fun onSpaceArenaChunkUnload(event: ChunkUnloadEvent) {
		if (event.world.name == "SpaceArena") event.isSaveChunk = false
	}

	@EventHandler
	fun onCreatureSpawn(event: CreatureSpawnEvent) {
		if (event.spawnReason == NETHER_PORTAL) event.isCancelled = true
	}
}