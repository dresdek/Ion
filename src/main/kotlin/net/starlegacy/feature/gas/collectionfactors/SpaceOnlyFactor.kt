package net.starlegacy.feature.gas.collectionfactors

import net.starlegacy.feature.space.SpaceWorlds.contains
import org.bukkit.Location

class SpaceOnlyFactor : CollectionFactor() {
	override fun factor(location: Location?): Boolean {
		return contains(location!!.world)
	}
}