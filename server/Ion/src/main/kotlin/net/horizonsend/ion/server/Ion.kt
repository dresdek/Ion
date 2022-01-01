package net.horizonsend.ion.server

import net.starlegacy.PLUGIN
import org.bukkit.Bukkit.getScheduler
import org.dynmap.DynmapAPI
import org.dynmap.DynmapCommonAPI
import org.dynmap.DynmapCommonAPIListener

// All Ion specific additions to the code are uses this class instead of the main StarLegacy class
// This is for future proofing, when/if we remove the main StarLegacy class, Ion's code should need little to no changes
// This is why the class is structured like a Bukkit JavaPlugin class, despite not being one.
class Ion {
	companion object {
		val plugin get() = PLUGIN

		lateinit var dynmapAPI: DynmapAPI
			private set
	}

	fun onEnable() {
		DynmapCommonAPIListener.register(Listener())

		getScheduler().runTaskAsynchronously(plugin, Runnable {
			plugin.manager.apply {
				registerCommand(QuickBalance)

				commandCompletions.registerCompletion("valueNames") {
					QuickBalance.balancedValues.keys
				}
			}
		})
	}

	class Listener: DynmapCommonAPIListener() {
		override fun apiEnabled(api: DynmapCommonAPI) {
			dynmapAPI = api as DynmapAPI
		}
	}
}