package net.horizonsend

import net.starlegacy.PLUGIN
import net.starlegacy.StarLegacy
import org.bukkit.Bukkit.getScheduler

class Ion {
	companion object {
		lateinit var pluginInstance: StarLegacy
			private set
	}

	init { pluginInstance = PLUGIN }

	fun onEnable() {
		getScheduler().runTaskAsynchronously(pluginInstance, Runnable {

		})
	}
}