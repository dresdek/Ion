package net.starlegacy.feature.gas.collectionfactors

import org.bukkit.Location

class SkyLightFactor(private val minimum: Int, private val maximum: Int) : CollectionFactor() {
	override fun factor(location: Location): Boolean {
		return location.block.lightFromSky <= maximum && location.block.lightFromSky >= minimum
	}
}