package net.horizonsend.ion.server.listeners.paper

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason.NETHER_PORTAL
import org.bukkit.event.world.ChunkUnloadEvent

/**
 * This object is used to make minor tweaks to vanilla behaviour using event listeners.
 */
object Tweaks: Listener {
	/**
	 * Event which listens for chunk unloading.
	 * If the world name is "SpaceArena" the chunk is not saved.
	 * TODO: This should be defined by a config file.
	 */
	@EventHandler
	fun onSpaceArenaChunkUnload(event: ChunkUnloadEvent) {
		if (event.world.name == "SpaceArena") event.isSaveChunk = false
	}

	/**
	 * Event which listens for creature spawns.
	 * If the creature was spawned by a nether portal, the spawn will be can
	 * This is to prevent pigman from spawning from airlocks.
	 */
	@EventHandler
	fun onCreatureSpawn(event: CreatureSpawnEvent) {
		if (event.spawnReason == NETHER_PORTAL) event.isCancelled = true
	}
}