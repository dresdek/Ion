package net.horizonsend.ion.server

import net.starlegacy.PLUGIN
import org.bukkit.Bukkit
import org.dynmap.DynmapAPI
import org.dynmap.DynmapCommonAPI
import org.dynmap.DynmapCommonAPIListener

class Ion {
	/**
	 * To make things statically accessable for convenience a companion object is used.
	 */
	companion object {
		val ionInstance get() = PLUGIN

		var dynmapAPI: DynmapAPI? = null
			protected set

		var protocolLib: ProtocolManager? = getProtocolManager()
	}

	/**
	 * Initializes Ion's additions, realistically this should be done in onEnable()
	 */
	init {
		DynmapCommonAPIListener.register(DynmapEnabledListener)

		protocolLib.registerPacketListener(PacketTweaks)

		Bukkit.getPluginManager().registerEvents(Tweaks, ionInstance)

		ionInstance.manager.apply {
			registerCommand(QuickBalance)

			commandCompletions.registerCompletion("valueNames") {
				QuickBalance.balancedValues.keys
			}
		}
	}
}