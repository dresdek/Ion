package net.horizonsend.ion.server

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
		DynmapCommonAPIListener.register(Listener())

		getScheduler().runTaskAsynchronously(plugin, Runnable {
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