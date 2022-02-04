package net.starlegacy.listener

import net.starlegacy.PLUGIN
import org.bukkit.Bukkit
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

abstract class SLEventListener : Listener {
	protected val log: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(javaClass)

	protected val plugin get() = PLUGIN

	fun register() {
		Bukkit.getPluginManager().registerEvents(this, PLUGIN)
		onRegister()
	}

	// attempt ?
	protected inline fun <reified T : Event> subscribe(
		priority: EventPriority = EventPriority.NORMAL,
		ignoreCancelled: Boolean = false,
		noinline block: (T) -> Unit =
	): Unit = plugin.listen(priority, ignoreCancelled, block)

	protected inline fun <reified T : Event> subscribe(
		priority: EventPriority = EventPriority.NORMAL,
		ignoreCancelled: Boolean = false,
		noinline block: (Listener, T) -> Unit
	): Unit = plugin.listen(priority, ignoreCancelled, block)

	protected open fun onRegister() {}

	open fun supportsVanilla(): Boolean = false
}
