package net.horizonsend.ion

import co.aikar.commands.PaperCommandManager
import kotlinx.serialization.ExperimentalSerializationApi
import net.starlegacy.PLUGIN
import org.bukkit.Bukkit.getScheduler

class Ion {
	companion object {
		val plugin get() = PLUGIN
	}

	@ExperimentalSerializationApi
	fun onEnable() {
		getScheduler().runTaskAsynchronously(plugin, Runnable {
			PaperCommandManager(plugin).apply {
				registerCommand(QuickBalance)

				commandCompletions.registerCompletion("valueNames") {
					QuickBalance.balancedValues.keys
				}
			}
		})
	}
}