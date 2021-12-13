package net.starlegacy.spacegenerator

import org.bukkit.World
import org.bukkit.block.Biome
import org.bukkit.generator.ChunkGenerator
import java.util.*

class SpaceChunkGenerator(worldConfig: SpaceGeneratorConfig.World?) : ChunkGenerator() {
	private val asteroidGenerator: AsteroidGenerator

	init {
		asteroidGenerator = AsteroidGenerator(worldConfig)
	}

	override fun isParallelCapable(): Boolean {
		return true
	}

	override fun generateChunkData(
		world: World,
		random: Random,
		x: Int,
		z: Int,
		biomeGrid: BiomeGrid
	): ChunkData {
		setBiome(world, biomeGrid)
		val chunkData = createChunkData(world)
		asteroidGenerator.addAsteroids(world, x, z, chunkData)
		return chunkData
	}

	private fun setBiome(world: World, biomeGrid: BiomeGrid) {
		val biome: Biome
		biome = when (world.environment) {
			World.Environment.NORMAL -> Biome.THE_VOID
			World.Environment.NETHER -> Biome.SOUL_SAND_VALLEY
			World.Environment.THE_END -> Biome.THE_END
			else -> throw IllegalStateException("Unexpected value: " + world.environment)
		}
		for (x in 0..15) {
			for (y in 0..255) {
				for (z in 0..15) {
					biomeGrid.setBiome(x, y, z, biome)
				}
			}
		}
	}
}