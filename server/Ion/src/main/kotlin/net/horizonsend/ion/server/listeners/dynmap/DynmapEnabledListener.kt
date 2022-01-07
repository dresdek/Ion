package net.horizonsend.ion.server.listeners.dynmap

object DynmapEnabledListener: DynmapCommonAPIListener() {
	override fun apiEnabled(api: DynmapCommonAPI) {
		dynmapAPI = api as DynmapAPI
	}
}