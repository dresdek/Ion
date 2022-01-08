package net.horizonsend.ion.server

import com.comphenix.protocol.ProtocolLibrary.getProtocolManager
import com.comphenix.protocol.ProtocolManager
import net.horizonsend.ion.server.listeners.dynmap.DynmapEnabledListener
import net.horizonsend.ion.server.listeners.paper.Tweaks
import net.horizonsend.ion.server.listeners.protocollib.LoginPacketListener
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

		var protocolLib: ProtocolManager? = getProtocolManager()
	}

	/**
	 * Initializes Ion's additions, realistically this should be done in onEnable()
	 */
	init {
		DynmapCommonAPIListener.register(DynmapEnabledListener)

		protocolLib?.addPacketListener(LoginPacketListener)

		Bukkit.getPluginManager().registerEvents(Tweaks, ionInstance)

		ionInstance.manager.apply {
			registerCommand(QuickBalance)

			commandCompletions.registerCompletion("valueNames") {
				QuickBalance.balancedValues.keys
			}
		}
	}
}