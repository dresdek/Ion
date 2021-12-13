package net.starlegacy.util.blockplacement

import com.google.common.base.Preconditions
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap
import java.util.function.LongFunction
import java.util.concurrent.atomic.AtomicInteger
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.chunk.LevelChunk
import org.bukkit.craftbukkit.v1_17_R1.CraftChunk
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.chunk.LevelChunkSection
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.lighting.LevelLightEngine
import net.starlegacy.util.*
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.World
import org.slf4j.LoggerFactory
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.function.BiConsumer
import java.util.function.Consumer

internal class BlockPlacementRaw {
	private val log = LoggerFactory.getLogger(javaClass)
	private val worldQueues = WeakHashMap<World, Long2ObjectOpenHashMap<Array<Array<Array<BlockState?>>>>>()
	@Synchronized
	fun queue(world: World, queue: Long2ObjectOpenHashMap<NMSBlockData>) {
		val worldQueue = worldQueues.computeIfAbsent(world) { w: World? -> Long2ObjectOpenHashMap() }
		addToWorldQueue(queue, worldQueue)
	}

	fun addToWorldQueue(
		queue: Long2ObjectOpenHashMap<NMSBlockData>,
		worldQueue: Long2ObjectOpenHashMap<Array<Array<Array<BlockState>>>>
	) {
		queue.forEach(BiConsumer { coords: Long?, blockData: BlockState? ->
			val y = blockKeyY(coords!!)
			val x = blockKeyX(coords)
			val z = blockKeyZ(coords)
			val chunkX = x shr 4
			val chunkZ = z shr 4
			val chunkKey = chunkKey(chunkX, chunkZ)
			val chunkQueue = worldQueue.computeIfAbsent(chunkKey, LongFunction { c: Long -> emptyChunkMap() })
			chunkQueue[y][x and 15][z and 15] = blockData
		})
	}

	fun flush(onComplete: Consumer<World?>?) {
		Preconditions.checkState(Bukkit.isPrimaryThread())
		if (worldQueues.isEmpty()) {
			return
		}
		for (world in ArrayList(worldQueues.keys)) {
			val worldQueue = worldQueues[world]!!
			placeWorldQueue(world, worldQueue, onComplete, false)
		}
	}

	fun placeWorldQueue(
		world: World,
		worldQueue: Long2ObjectOpenHashMap<Array<Array<Array<BlockState>>>>,
		onComplete: ((World) -> Unit)?,
		immediate: Boolean
	) {
		if (worldQueue.isEmpty()) {
			log.debug("Queue for  " + world.name + " was empty!")
			worldQueues.remove(world, worldQueue)
			return
		}
		val start = System.nanoTime()
		val placedChunks = AtomicInteger()
		val placed = AtomicInteger()
		val chunkCount: Int = worldQueue.size
		log.debug("Queued " + chunkCount + " chunks for " + world.name)
		for ((chunkKey, blocks) in worldQueue.long2ObjectEntrySet()) {
			actuallyPlaceChunk(world, onComplete, start, placedChunks, placed, chunkCount, chunkKey, blocks, immediate)
		}
		if (worldQueues.remove(world, worldQueue)) {
			worldQueue.clear()
		}
	}

	private fun actuallyPlaceChunk(
		world: World, onComplete: Consumer<World?>?, start: Long, placedChunks: AtomicInteger,
		placed: AtomicInteger, chunkCount: Int, chunkKey: Long, blocks: Array<Array<Array<BlockState?>>>, immediate: Boolean
	) {
		val cx = chunkKeyX(chunkKey)
		val cz = chunkKeyZ(chunkKey)
		val isLoaded = world.isChunkLoaded(cx, cz)
		if (!isLoaded && !immediate) {
			world.getChunkAtAsync(cx, cz).thenAccept { chunk: Chunk ->
				actuallyPlaceChunk(
					world,
					onComplete,
					start,
					placedChunks,
					placed,
					chunkCount,
					blocks,
					cx,
					cz,
					false,
					chunk
				)
			}
			return
		}
		val chunk = world.getChunkAt(cx, cz)
		actuallyPlaceChunk(world, onComplete, start, placedChunks, placed, chunkCount, blocks, cx, cz, isLoaded, chunk)
	}

	private fun updateHeightMap(heightMap: Heightmap?, x: Int, y: Int, z: Int, iBlockData: BlockState) {
		heightMap?.update(x and 15, y, z and 15, iBlockData)
	}

	private fun actuallyPlaceChunk(
		world: World, onComplete: Consumer<World?>?, start: Long,
		placedChunks: AtomicInteger, placed: AtomicInteger, chunkCount: Int,
		blocks: Array<Array<Array<BlockState?>>>, cx: Int, cz: Int, wasLoaded: Boolean, chunk: Chunk
	) {
		val nmsChunk = (chunk as CraftChunk).handle
		val nmsWorld = nmsChunk.level
		val sections = nmsChunk.sections
		var section: LevelChunkSection? = null
		var localPlaced = 0
		var bitmask = 0 // used for the player chunk update thing to let it know which chunks to update
		val motionBlocking: Heightmap = nmsChunk.heightMap.get(Heightmap.Types.MOTION_BLOCKING)
		val motionBlockingNoLeaves: Heightmap = nmsChunk.heightMap.get(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES)
		val oceanFloor: Heightmap = nmsChunk.heightMap.get(Heightmap.Types.OCEAN_FLOOR)
		val worldSurface: Heightmap = nmsChunk.heightMap.get(Heightmap.Types.WORLD_SURFACE)
		for (y in blocks.indices) {
			val sectionY = y shr 4
			if (section == null || sectionY != section.getYPosition()) {
				section = sections[sectionY]
				if (section == null) {
					section = LevelChunkSection(sectionY shl 4, nmsChunk, nmsWorld, true)
					sections[sectionY] = section
				}
			}
			val xBlocks = blocks[y]
			for (x in xBlocks.indices) {
				val zBlocks = xBlocks[x]
				for (z in zBlocks.indices) {
					val newData = zBlocks[z] ?: continue
					val oldData: BlockState = section.getType(x, y and 15, z)
					if (oldData.block is BaseEntityBlock && oldData.block !== newData.block) {
						val pos: BlockPos = nmsChunk.pos.asPosition().add(x, y, z)
						nmsWorld.removeTileEntity(pos)
					}
					section.setType(x, y and 15, z, newData)
					updateHeightMap(motionBlocking, x, y, z, newData)
					updateHeightMap(motionBlockingNoLeaves, x, y, z, newData)
					updateHeightMap(oceanFloor, x, y, z, newData)
					updateHeightMap(worldSurface, x, y, z, newData)
					localPlaced++
				}
			}
			bitmask = bitmask or (1 shl sectionY) // update the bitmask to include this section
		}
		relight(world, cx, cz, nmsWorld)
		sendChunkPacket(nmsChunk, bitmask)
		nmsChunk.setNeedsSaving(true)
		if (!wasLoaded) {
			world.unloadChunkRequest(cx, cz)
		}
		val placedNow = placed.addAndGet(localPlaced)
		val placedChunksNow = placedChunks.incrementAndGet()
		if (placedChunksNow == chunkCount) {
			val elapsed = System.nanoTime() - start
			val elapsedMs = TimeUnit.NANOSECONDS.toMillis(elapsed)
			log.debug(" ===> Placed " + placed + " blocks in " + elapsedMs + "ms")
			onComplete?.accept(world)
		} else {
			log.debug("Placed $placedNow blocks and $placedChunksNow/$chunkCount chunks ")
		}
	}

	private fun relight(world: World, cx: Int, cz: Int, nmsWorld: ServerLevel) {
		val lightEngine: LevelLightEngine = nmsWorld.getChunkProvider().getLightEngine()
		lightEngine.b(ChunkCoordIntPair(cx, cz), world.environment == World.Environment.NORMAL)
	}

	private fun sendChunkPacket(nmsChunk: LevelChunk, bitmask: Int) {
		val playerChunk = nmsChunk.playerChunk ?: return
		val packet = PacketPlayOutMapChunk(nmsChunk, bitmask)
		playerChunk.sendPacketToTrackedPlayers(packet, false)
	}

	companion object {
		private fun emptyChunkMap(): Array<Array<Array<BlockState?>>> {
			// y x z array
			val array: Array<Array<Array<BlockState?>>?> = arrayOfNulls(256)
			for (y1 in array.indices) {
				val xArray: Array<Array<BlockState?>?> = arrayOfNulls(16)
				for (x1 in xArray.indices) {
					xArray[x1] = arrayOfNulls(16)
				}

				array[y1] = xArray.requireNoNulls()
			}
			return array.requireNoNulls()
		}

		private const val ignoreOldData = true // if false, client will recalculate lighting based on old/new chunk data
	}
}