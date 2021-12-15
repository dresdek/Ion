package net.starlegacy.feature.starship

import net.starlegacy.util.setDisplayNameAndGet
import net.starlegacy.util.setLoreAndGet
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

enum class StarshipType(
	val displayName: String,
	val minSize: Int,
	val maxSize: Int,
	val minLevel: Int,
	val containerPercent: Double,
	val crateLimitMultiplier: Double,
	val sneakFlyAccelDistance: Int,
	val maxSneakFlyAccel: Int,
	val interdictionRange: Int,
	val hyperspaceRangeMultiplier: Double,
	menuItemMaterial: Material,
	val isWarship: Boolean
) {
	STARFIGHTER(
		displayName = "Starfighter",
		minSize = 250,
		maxSize = 500,
		minLevel = 1,
		containerPercent = 0.025,
		crateLimitMultiplier = 0.5,
		sneakFlyAccelDistance = 4,
		maxSneakFlyAccel = 4,
		interdictionRange = 400,
		hyperspaceRangeMultiplier = 2.0,
		menuItemMaterial = Material.COPPER_INGOT,
		isWarship = true
	),
	CORVETTE(
		displayName = "Corvette",
		minSize = 500,
		maxSize = 2000,
		minLevel = 12,
		containerPercent = 0.025,
		crateLimitMultiplier = 0.5,
		sneakFlyAccelDistance = 5,
		maxSneakFlyAccel = 2,
		interdictionRange = 800,
		hyperspaceRangeMultiplier = 2.1,
		menuItemMaterial = Material.CUT_COPPER_SLAB,
		isWarship = true
	),
	FRIGATE(
		displayName = "Frigate",
		minSize = 2000,
		maxSize = 4000,
		minLevel = 24,
		containerPercent = 0.025,
		crateLimitMultiplier = 0.5,
		sneakFlyAccelDistance = 6,
		maxSneakFlyAccel = 2,
		interdictionRange = 1200,
		hyperspaceRangeMultiplier = 2.2,
		menuItemMaterial = Material.CUT_COPPER_STAIRS,
		isWarship = true
	),
	DESTROYER(
		displayName = "Destroyer",
		minSize = 4000,
		maxSize = 8000,
		minLevel = 36,
		containerPercent = 0.025,
		crateLimitMultiplier = 0.5,
		sneakFlyAccelDistance = 6,
		maxSneakFlyAccel = 2,
		interdictionRange = 1600,
		hyperspaceRangeMultiplier = 2.3,
		menuItemMaterial = Material.CUT_COPPER,
		isWarship = true
	),
	CRUISER(
		displayName = "Cruiser",
		minSize = 8000,
		maxSize = 12000,
		minLevel = 48,
		containerPercent = 0.025,
		crateLimitMultiplier = 0.5,
		sneakFlyAccelDistance = 5,
		maxSneakFlyAccel = 3,
		interdictionRange = 2000,
		hyperspaceRangeMultiplier = 2.4,
		menuItemMaterial = Material.IRON_BLOCK,
		isWarship = true
	),
	SHUTTLE(
		displayName = "Shuttle",
		minSize = 100,
		maxSize = 500,
		minLevel = 1,
		containerPercent = 0.045,
		crateLimitMultiplier = 1.0,
		sneakFlyAccelDistance = 5,
		maxSneakFlyAccel = 2,
		interdictionRange = 300,
		hyperspaceRangeMultiplier = 1.0,
		menuItemMaterial = Material.STICK,
		isWarship = false
	),
	TRANSPORT(
		displayName = "Transport",
		minSize = 500,
		maxSize = 2000,
		minLevel = 12,
		containerPercent = 0.045,
		crateLimitMultiplier = 1.0,
		sneakFlyAccelDistance = 10,
		maxSneakFlyAccel = 3,
		interdictionRange = 600,
		hyperspaceRangeMultiplier = 1.1,
		menuItemMaterial = Material.OAK_SLAB,
		isWarship = false
	),
	LIGHT_FREIGHTER(
		displayName = "Light Freighter",
		minSize = 2000,
		maxSize = 4000,
		minLevel = 24,
		containerPercent = 0.045,
		crateLimitMultiplier = 1.0,
		sneakFlyAccelDistance = 10,
		maxSneakFlyAccel = 3,
		interdictionRange = 900,
		hyperspaceRangeMultiplier = 1.2,
		menuItemMaterial = Material.OAK_STAIRS,
		isWarship = false
	),
	MEDIUM_FREIGHTER(
		displayName = "Medium Freighter",
		minSize = 4000,
		maxSize = 8000,
		minLevel = 36,
		containerPercent = 0.045,
		crateLimitMultiplier = 1.0,
		sneakFlyAccelDistance = 10,
		maxSneakFlyAccel = 3,
		interdictionRange = 1200,
		hyperspaceRangeMultiplier = 1.3,
		menuItemMaterial = Material.OAK_PLANKS,
		isWarship = false
	),
	BULK_FREIGHTER(
		displayName = "Bulk Freighter",
		minSize = 8000,
		maxSize = 12000,
		minLevel = 48,
		containerPercent = 0.045,
		crateLimitMultiplier = 1.0,
		sneakFlyAccelDistance = 10,
		maxSneakFlyAccel = 3,
		interdictionRange = 1500,
		hyperspaceRangeMultiplier = 1.4,
		menuItemMaterial = Material.COBBLESTONE,
		isWarship = false
	);

	val menuItem: ItemStack = ItemStack(menuItemMaterial)
		.setDisplayNameAndGet(displayName)
		.setLoreAndGet(
			listOf(
				"Min Block Count: $minSize",
				"Max Block Count: $maxSize",
				"Min Level: $minLevel",
				"Max Container:Total Blocks Ratio: $containerPercent",
				"Crate Limit Multiplier: $crateLimitMultiplier",
				"Sneak Fly Accel Distance: $sneakFlyAccelDistance",
				"Max Sneak Fly Accel: $maxSneakFlyAccel",
				"Interdiction Range: $interdictionRange",
				"Hyperspace Range Multiplier: $hyperspaceRangeMultiplier",
				"Warship: $isWarship"
			)
		)

	companion object {
		private val stringMap = mutableMapOf<String, StarshipType>().apply {
			putAll(values().associateBy { it.name.lowercase() })
			putAll(values().associateBy { it.displayName.lowercase() })

			println(this)
		}

		fun getType(name: String): StarshipType? = stringMap[name.lowercase()]

		fun getUnlockedTypes(player: Player): List<StarshipType> = values().sortedBy { it.minLevel }
	}
}
