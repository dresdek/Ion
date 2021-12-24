package net.starlegacy.util

import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.X
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.X_ROT
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Y
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Z
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.util.Vector

object ConnectionUtils {
	private val OFFSET_DIRECTION = setOf(X_ROT, Y_ROT)
	private val OFFSET_ALL = setOf(X_ROT, Y_ROT, X, Y, Z)

	fun move(player: Player, loc: Location, theta: Float = 0.0f, offsetPos: Vector? = null) {
		val handle = (player as CraftPlayer).handle
		val connection = handle.connection
		val x = loc.x
		val y = loc.y
		val z = loc.z

		if (handle.containerMenu !== handle.inventoryMenu) {
			handle.closeContainer()
		}

		val px: Double
		val py: Double
		val pz: Double

		if (offsetPos == null) {
			px = x
			py = y
			pz = z
		} else {
			px = offsetPos.x
			py = offsetPos.y
			pz = offsetPos.z
		}

		handle.setPos(x, y, z)
		handle.setRot(handle.yRot + theta, handle.xRot)

		val flags = if (offsetPos != null) OFFSET_ALL else OFFSET_DIRECTION
		val packet = ClientboundPlayerPositionPacket(px, py, pz, theta, 0f, flags, 0, true)
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
