package net.horizonsend.ion.server

import net.starlegacy.PLUGIN
import org.bukkit.Bukkit
import org.dynmap.DynmapAPI
import org.dynmap.DynmapCommonAPI
import org.dynmap.DynmapCommonAPIListener

// All Ion specific additions to the code are uses this class instead of the main StarLegacy class
// This is for future proofing, when/if we remove the main StarLegacy class, Ion's code should require minimal changes
// This is why the class is structured like a Bukkit JavaPlugin class, despite not being one.
class Ion {
	companion object {
		val ionInstance get() = PLUGIN

		var dynmapAPI: DynmapAPI? = null
			private set

		var protocolLib: ProtocolManager? = getProtocolManager()
	}

	fun onEnable() {
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