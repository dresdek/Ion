package net.starlegacy.util

import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.X
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.X_ROT
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Y
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Z
import net.minecraft.server.network.ServerGamePacketListenerImpl
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.lang.System.currentTimeMillis
import java.lang.reflect.Field

object ConnectionUtils {
	private val OFFSET_DIRECTION = setOf(X_ROT, Y_ROT)
	private val OFFSET_ALL = setOf(X_ROT, Y_ROT, X, Y, Z)

	private var justTeleportedField: Field = getField("justTeleported")

	@Throws(NoSuchFieldException::class)
	private fun getField(name: String): Field {
		val field = ServerGamePacketListenerImpl::class.java.getDeclaredField(name)
		field.isAccessible = true
		return field
	}

	fun move(player: Player, loc: Location, theta: Float = 0.0f, offsetPos: Vector? = null) {
		val handle = (player as CraftPlayer).handle
		val connection = handle.connection

		justTeleportedField.set(connection, true)

		if (handle.containerMenu !== handle.inventoryMenu) handle.closeContainer()

		val px: Double = offsetPos?.x ?: loc.x
		val py: Double = offsetPos?.y ?: loc.y
		val pz: Double = offsetPos?.z ?: loc.z

		handle.setPos(loc.x, loc.y, loc.z)
		handle.setRot(handle.yRot + theta, handle.xRot)

		val flags = if (offsetPos != null) OFFSET_ALL else OFFSET_DIRECTION
		val packet = ClientboundPlayerPositionPacket(px, py, pz, theta, 0f, flags, (currentTimeMillis() / 1000).toInt(), true)
		connection.send(packet)
	}

	fun teleport(player: Player, loc: Location) {
		move(player, loc, 0.0f, null)
	}

	fun teleportRotate(player: Player, loc: Location, theta: Float) {
		move(player, loc, theta, null)
	}

	fun move(player: Player, loc: Location, dx: Double, dy: Double, dz: Double) {
		move(player, loc, 0.0f, Vector(dx, dy, dz))
	}
}
