package net.horizonsend.ion.server.listeners.protocollib

object PacketTweaks: PacketAdapter(this, ListenerPriority.HIGHEST, PacketType.Play.Server.LOGIN) {
	fun onPacketSending(event: PacketEvent) {
		if (event.packetType != PacketType.Play.Server.LOGIN) return

		event.packet.integers.write(2, 32) // Set render distance to 32
	}
}