package net.starlegacy.feature.gas.collectionfactors

import net.starlegacy.feature.gas.collectionfactors.CollectionFactor
import org.bukkit.Location
import java.util.*

class WorldLimitFactor(private val enabledWorlds: Array<String>) : CollectionFactor() {
	override fun factor(location: Location): Boolean {
		return Arrays.stream(enabledWorlds)
			.anyMatch { anotherString: String? -> location.world.name.equals(anotherString, ignoreCase = true) }
	}
}