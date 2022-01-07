package net.horizonsend.ion.server.listeners.dynmap

/**
 * To make the dynmapAPI accessable to the plugin, we must wait for it to load, this event listener will be triggered
 * when this happens.
 */
object DynmapEnabledListener: DynmapCommonAPIListener() {
	override fun apiEnabled(api: DynmapCommonAPI) {
		dynmapAPI = api as DynmapAPI
	}
}