package net.starlegacy.feature.gas.collectionfactors

import net.starlegacy.util.LegacyBlockUtils.isInside
import org.bukkit.Location

class OutdoorsFactor : CollectionFactor() {
	override fun factor(location: Location?): Boolean {
		return !isInside(location!!, 2)
	}
}