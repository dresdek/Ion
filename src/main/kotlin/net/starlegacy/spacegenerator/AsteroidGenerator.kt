package net.starlegacy.spacegenerator

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import net.starlegacy.spacegenerator.SpaceGeneratorConfig.World.AsteroidBelt
import net.starlegacy.spacegenerator.asteroid.AsteroidData
import net.starlegacy.spacegenerator.asteroid.AsteroidOreDistribution
import net.starlegacy.spacegenerator.asteroid.CachedAsteroid
import org.bukkit.World
import org.bukkit.generator.ChunkGenerator
import org.slf4j.LoggerFactory
import java.util.*

// java used here to optimize performance
class AsteroidGenerator(private val worldConfig: SpaceGeneratorConfig.World) {
	private val log = LoggerFactory.getLogger(javaClass)
	private val randomAsteroidDistribution: AsteroidOreDistribution
	private val beltDistributions: LoadingCache<AsteroidBelt, AsteroidOreDistribution>

	init {
		randomAsteroidDistribution = AsteroidOreDistribution(worldConfig.randomAsteroidOreDistribution)
		beltDistributions = CacheBuilder.newBuilder()
			.weakKeys()
			.build(CacheLoader.from { belt: AsteroidBelt? -> getBeltDistribution(belt) })
	}

	private fun getBeltDistribution(belt: AsteroidBelt?): AsteroidOreDistribution {
		return if (belt == null) {
			AsteroidOreDistribution(HashMap())
		} else AsteroidOreDistribution(belt.oreDistribution)
	}

	fun addAsteroids(world: World, x: Int, z: Int, data: ChunkGenerator.ChunkData) {
		val random = Random()
		val worldSeed = world.seed
		random.setSeed(worldSeed)
		val xRand = random.nextLong() / 2L * 2L + 1L
		val zRand = random.nextLong() / 2L * 2L + 1L
		for (neighborX in x - 8..x + 8) {
			for (neighborZ in z - 8..z + 8) {
				val neighborSeed = neighborX.toLong() * xRand + neighborZ.toLong() * zRand xor worldSeed
				random.setSeed(neighborSeed)
				checkBeltAsteroids(world, x, z, data, neighborX, neighborZ, random)
				checkRandomAsteroid(world, x, z, data, neighborX, neighborZ, random)
			}
		}
	}

	private fun checkBeltAsteroids(
		world: World,
		x: Int,
		z: Int,
		data: ChunkGenerator.ChunkData,
		neighborX: Int,
		neighborZ: Int,
		random: Random
	) {
		val blockX = neighborX shl 4
		val blockZ = neighborZ shl 4
		for (belt in worldConfig.asteroidBelts) {
			val distance =
				Math.sqrt(Math.pow((blockX - belt.x).toDouble(), 2.0) + Math.pow((blockZ - belt.z).toDouble(), 2.0))
			val distanceFromRadius = Math.abs(belt.radius - distance)
			if (distanceFromRadius >= belt.thickness) {
				continue
			}
			checkBeltAsteroid(world, x, z, data, neighborX, neighborZ, random, belt)
		}
	}

	private fun checkRandomAsteroid(
		world: World,
		chunkX: Int,
		chunkZ: Int,
		data: ChunkGenerator.ChunkData,
		neighborChunkX: Int,
		neighborChunkZ: Int,
		random: Random
	) {
		val asteroidChance = random.nextFloat()
		if (asteroidChance > 1.0f / worldConfig.randomAsteroidSparsity) {
			return
		}

		// todo: pre-generate an array of cached asteroids to remove string lookup
		val asteroids = worldConfig.randomAsteroids
		tryPlaceAsteroidSection(
			world,
			chunkX,
			chunkZ,
			data,
			neighborChunkX,
			neighborChunkZ,
			random,
			asteroids,
			RANDOM_ASTEROID_PADDING,
			randomAsteroidDistribution
		)
	}

	private fun checkBeltAsteroid(
		world: World,
		chunkX: Int,
		chunkZ: Int,
		data: ChunkGenerator.ChunkData,
		neighborChunkX: Int,
		neighborChunkZ: Int,
		random: Random,
		belt: AsteroidBelt
	) {
		if (belt.asteroids.isEmpty()) {
			return
		}
		val asteroidChance = random.nextFloat()
		if (asteroidChance > 1.0f / belt.sparsity) {
			return
		}
		val asteroids = belt.asteroids
		val distribution = beltDistributions.getUnchecked(belt)
		tryPlaceAsteroidSection(
			world,
			chunkX,
			chunkZ,
			data,
			neighborChunkX,
			neighborChunkZ,
			random,
			asteroids,
			BELT_PADDING,
			distribution
		)
	}

	private fun tryPlaceAsteroidSection(
		world: World,
		chunkX: Int,
		chunkZ: Int,
		data: ChunkGenerator.ChunkData,
		neighborChunkX: Int,
		neighborChunkZ: Int,
		random: Random,
		asteroidNames: List<String>,
		padding: Int,
		distribution: AsteroidOreDistribution
	) {
		if (asteroidNames.isEmpty()) {
			return
		}

		// todo: pre-generate an array of cached asteroids to remove string lookup
		val asteroidIndex = random.nextInt(asteroidNames.size)
		val asteroidName = asteroidNames[asteroidIndex]
		val asteroid = AsteroidData.cachedAsteroids[asteroidName]!!.value
		val width = asteroid.width
		val height = asteroid.height
		val length = asteroid.length
		val yBound = world.maxHeight - height - padding * 2
		val asteroidX = random.nextInt(16) + (neighborChunkX shl 4)
		val asteroidY = random.nextInt(yBound) + padding
		val asteroidZ = random.nextInt(16) + (neighborChunkZ shl 4)
		val rotationMatrixIndex = random.nextInt(rotationMatrices.size)
		val matrix = rotationMatrices[rotationMatrixIndex]
		val row1 = matrix[0]
		val row2 = matrix[1]
		val row3 = matrix[2]

		// this assumes that all coordinates within cached asteroid are relative
		// to its min point, and if you add width/height/length, you get max point
		val farthestX = asteroidX + width * row1[0] + height * row1[1] + length * row1[2]
		val farthestY = asteroidY + width * row2[0] + height * row2[1] + length * row2[2]
		val farthestZ = asteroidZ + width * row3[0] + height * row3[1] + length * row3[2]
		val minX = Math.min(asteroidX, farthestX)
		val minY = Math.min(asteroidY, farthestY)
		val minZ = Math.min(asteroidZ, farthestZ)
		val maxX = Math.max(asteroidX, farthestX)
		val maxY = Math.max(asteroidY, farthestY)
		val maxZ = Math.max(asteroidZ, farthestZ)
		val maxHeight = world.maxHeight
		if (minY < 0 || maxY >= maxHeight) {
			return
		}
		val minChunkX = minX shr 4
		val minChunkZ = minZ shr 4
		val maxChunkX = maxX shr 4
		val maxChunkZ = maxZ shr 4
		if (chunkX < minChunkX || chunkX > maxChunkX || chunkZ < minChunkZ || chunkZ > maxChunkZ) {
			return
		}
		placeBlocks(
			chunkX,
			chunkZ,
			data,
			neighborChunkX,
			neighborChunkZ,
			random,
			asteroidName,
			asteroid,
			distribution,
			asteroidX,
			asteroidY,
			asteroidZ,
			rotationMatrixIndex,
			row1,
			row2,
			row3,
			maxHeight
		)
	}

	private fun placeBlocks(
		chunkX: Int,
		chunkZ: Int,
		data: ChunkGenerator.ChunkData,
		neighborChunkX: Int,
		neighborChunkZ: Int,
		random: Random,
		asteroidName: String,
		asteroid: CachedAsteroid,
		distribution: AsteroidOreDistribution,
		asteroidX: Int,
		asteroidY: Int,
		asteroidZ: Int,
		rotationMatrixIndex: Int,
		row1: IntArray,
		row2: IntArray,
		row3: IntArray,
		maxHeight: Int
	) {
		// TODO: Make this ordered by *something* so that the seed determines the ore locations, not map randomness
		for (entry in asteroid.blocks.entries) {
			val (dx, dy, dz) = entry.key
			var blockData = entry.value
			val x = asteroidX + dx * row1[0] + dy * row1[1] + dz * row1[2]
			val y = asteroidY + dx * row2[0] + dy * row2[1] + dz * row2[2]
			val z = asteroidZ + dx * row3[0] + dy * row3[1] + dz * row3[2]

			// only paste the parts in the current chunk
			if (x shr 4 != chunkX || z shr 4 != chunkZ) {
				continue
			}
			if (y < 0 || y >= maxHeight) {
				log.warn(
					"Placing asteroid " + asteroidName
						+ " at " + asteroidX + ", " + asteroidY + "," + asteroidZ
						+ " rotation " + rotationMatrixIndex
						+ " with block at invalid Y level " + x + ", " + y + ", " + z
						+ " in chunk " + chunkX + ", " + chunkZ
						+ " from neighbor " + neighborChunkX + ", " + neighborChunkZ
				)
				continue
			}
			val material = blockData.material
			if (AsteroidData.ORE_REPLACE_TYPES.contains(material)) {
				blockData = distribution.pickType(blockData, random)
			}
			data.setBlock(x and 15, y, z and 15, blockData)
		}
	}

	companion object {
		private const val RANDOM_ASTEROID_PADDING = 5
		private const val BELT_PADDING = 50
		private val rotationMatrices = arrayOf(
			arrayOf(intArrayOf(+1, +0, +0), intArrayOf(+0, +1, +0), intArrayOf(+0, +0, +1)),
			arrayOf(intArrayOf(+0, +0, -1), intArrayOf(+0, +1, +0), intArrayOf(+1, +0, +0)),
			arrayOf(intArrayOf(+-1, +0, +0), intArrayOf(+0, +1, +0), intArrayOf(+0, +0, -1)),
			arrayOf(intArrayOf(+0, +0, +1), intArrayOf(+0, +1, +0), intArrayOf(+1, +0, +0)),
			arrayOf(intArrayOf(+1, +0, +0), intArrayOf(+0, +0, -1), intArrayOf(+0, +1, +0)),
			arrayOf(intArrayOf(+1, +0, +0), intArrayOf(+0, +0, +1), intArrayOf(+0, -1, +0))
		)
	}
}