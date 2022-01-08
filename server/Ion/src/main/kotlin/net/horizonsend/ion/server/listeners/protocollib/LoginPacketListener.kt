package net.horizonsend.ion.server.listeners.protocollib

import com.comphenix.protocol.PacketType.Play.Server.LOGIN
import com.comphenix.protocol.events.ListenerPriority.HIGHEST
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import net.horizonsend.ion.server.Ion.Companion.ionInstance

/**
 * As part of code to enable ship movements to be seen beyond the normal render distance of the server, this event
 * listener is used to lie to the client as to the server's render distance.
 */
object LoginPacketListener: PacketAdapter(ionInstance, HIGHEST, LOGIN) {
	override fun onPacketSending(event: PacketEvent) {
		if (event.packetType != LOGIN) return

		event.packet.integers.write(2, 32) // Set render distance to 32
	}
}