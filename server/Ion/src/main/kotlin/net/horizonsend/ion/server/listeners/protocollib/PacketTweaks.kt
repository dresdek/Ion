package net.horizonsend.ion.server.listeners.protocollib

/**
 * As part of code to enable ship movements to be seen beyond the normal render distance of the server, this event
 * listener is used to lie to the client as to the server's render distance.
 */
object PacketTweaks: PacketAdapter(this, ListenerPriority.HIGHEST, PacketType.Play.Server.LOGIN) {
	fun onPacketSending(event: PacketEvent) {
		if (event.packetType != PacketType.Play.Server.LOGIN) return

		event.packet.integers.write(2, 32) // Set render distance to 32
	}
}