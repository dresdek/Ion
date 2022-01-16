package net.horizonsend.ion.server

import net.horizonsend.ion.server.listeners.commands.QuickBalance
import net.horizonsend.ion.server.listeners.commands.starships.Listships
import net.horizonsend.ion.server.listeners.dynmap.DynmapEnabledListener
import net.horizonsend.ion.server.listeners.paper.Tweaks
import net.starlegacy.PLUGIN
import org.bukkit.Bukkit
import org.dynmap.DynmapAPI
import org.dynmap.DynmapCommonAPIListener

class Ion {
	/**
	 * To make things statically accessable for convenience a companion object is used.
	 */
	companion object {
		val ionInstance get() = PLUGIN

		var dynmapAPI: DynmapAPI? = null
	}

	/**
	 * Initializes Ion's additions, realistically this should be done in onEnable()
	 */
	init {
		DynmapCommonAPIListener.register(DynmapEnabledListener)

		Bukkit.getPluginManager().registerEvents(Tweaks, ionInstance)

		ionInstance.manager.apply {
			registerCommand(Listships)
			registerCommand(QuickBalance)

			commandCompletions.registerCompletion("valueNames") {
				QuickBalance.balancedValues.keys
			}
		}
	}
}