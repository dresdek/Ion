package net.horizonsend.ion.server

import net.starlegacy.PLUGIN
import org.bukkit.Bukkit.getScheduler
import org.dynmap.DynmapAPI
import org.dynmap.DynmapCommonAPI
import org.dynmap.DynmapCommonAPIListener

// All Ion specific additions to the code are uses this class instead of the main StarLegacy class
// This is for future proofing, when/if we remove the main StarLegacy class, Ion's code should require minimal changes
// This is why the class is structured like a Bukkit JavaPlugin class, despite not being one.
class Ion {
	companion object {
		lateinit var ionInstance: Ion private set
	}

	var dynmapAPI: DynmapAPI? = null
		private set

	// Bukkit JavaPlugin emulation
	val dataFolder get() = PLUGIN.dataFolder
	val log4JLogger get() = PLUGIN.log4JLogger

	// StarLegacy JavaPlugin emulation
	val manager get() = PLUGIN.manager

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