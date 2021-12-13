package net.starlegacy.feature.gas.collectionfactors

import net.starlegacy.feature.gas.collectionfactors.CollectionFactor
import net.starlegacy.feature.gas.collectionfactors.AtmosphereHeightFactor
import net.starlegacy.feature.gas.collectionfactors.DistanceFactor
import net.starlegacy.feature.gas.collectionfactors.RandomFactor
import net.starlegacy.feature.gas.collectionfactors.HyperspaceOnlyFactor
import net.starlegacy.feature.gas.collectionfactors.SpaceOnlyFactor
import net.starlegacy.feature.gas.collectionfactors.WorldLimitFactor
import net.starlegacy.feature.gas.collectionfactors.WorldChanceFactor
import net.starlegacy.feature.gas.collectionfactors.OutdoorsFactor
import org.bukkit.Bukkit
import org.bukkit.Location
import java.util.ArrayList

abstract class CollectionFactor {
	abstract fun factor(location: Location?): Boolean

	companion object {
		fun collectionSetFromString(string: String): List<CollectionFactor> {
			val collectionFactors: MutableList<CollectionFactor> = ArrayList()
			for (text in string.split(";").toTypedArray()) collectionFactors.add(valueOf(text))
			return collectionFactors
		}

		private fun valueOf(text: String): CollectionFactor {
			val params = text.split(":").toTypedArray()
			when (params[0].lowercase()) {
				"atmosphereheight" -> return AtmosphereHeightFactor(
					params[1].toInt().toDouble(), params[2].toInt().toDouble()
				)
				"distance" -> {
					val locationParams = params[1].split(",").toTypedArray()
					return DistanceFactor(
						Location(
							Bukkit.getWorld(locationParams[0]), locationParams[1].toInt()
								.toDouble(), locationParams[2].toInt().toDouble(), locationParams[3].toInt().toDouble()
						), params[2].toInt().toDouble(), params[3].toFloat()
					)
				}
				"random" -> return RandomFactor(params[1].toFloat())
				"hyperspaceonly" -> return HyperspaceOnlyFactor()
				"spaceonly" -> return SpaceOnlyFactor()
				"worldlimit" -> return WorldLimitFactor(params[1].split(",").toTypedArray())
				"worldchance" -> return WorldChanceFactor(params[1].toFloat(), params[2])
				"skylight", "outdoors" -> return OutdoorsFactor()
			}
			return RandomFactor(1.0f)
		}
	}
}