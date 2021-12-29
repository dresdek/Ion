package net.horizonsend.ion

import co.aikar.commands.PaperCommandManager
import net.starlegacy.PLUGIN
import org.bukkit.Bukkit.getScheduler
import org.dynmap.DynmapAPI
import org.dynmap.DynmapCommonAPI
import org.dynmap.DynmapCommonAPIListener

class Ion {
	companion object {
		val plugin get() = PLUGIN

		lateinit var dynmapAPI: DynmapAPI
			private set
	}

	fun onEnable() {
		getScheduler().runTaskAsynchronously(plugin, Runnable {
			DynmapCommonAPIListener.register(Listener())

			PaperCommandManager(plugin).apply {
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