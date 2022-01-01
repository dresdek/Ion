package net.horizonsend.ion.server

import net.starlegacy.PLUGIN
import net.starlegacy.StarLegacy
import org.bukkit.Bukkit.getScheduler
import org.dynmap.DynmapAPI
import org.dynmap.DynmapCommonAPI
import org.dynmap.DynmapCommonAPIListener

// All Ion specific additions to the code are uses this class instead of the main StarLegacy class
// This is for future proofing, when/if we remove the main StarLegacy class, Ion's code should require minimal changes
// This is why the class is structured like a Bukkit JavaPlugin class, despite not being one.
class Ion {
	companion object {
		val ionInstance get() = PLUGIN

		lateinit var realIonInstance: Ion
			private set
	}

	var dynmapAPI: DynmapAPI? = null

	fun onEnable() {
		DynmapCommonAPIListener.register(Listener())

		getScheduler().runTaskAsynchronously(PLUGIN, Runnable {
			ionInstance.manager.apply {
				registerCommand(QuickBalance)

				commandCompletions.registerCompletion("valueNames") {
					QuickBalance.balancedValues.keys
				}
			}
		})
	}

	class Listener: DynmapCommonAPIListener() {
		override fun apiEnabled(api: DynmapCommonAPI) {
			ionInstance.dynmapAPI = api as DynmapAPI
		}
	}
}

var StarLegacy.dynmapAPI
	get() = Ion.realIonInstance.dynmapAPI
	set(value) {
		Ion.realIonInstance.dynmapAPI = value
	}