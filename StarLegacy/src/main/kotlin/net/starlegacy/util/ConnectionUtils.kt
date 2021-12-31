package net.starlegacy.util

import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.X
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.X_ROT
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Y
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Z
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.util.Vector

object ConnectionUtils {
	private val OFFSET_DIRECTION = setOf(X_ROT, Y_ROT)
	private val OFFSET_ALL = setOf(X_ROT, Y_ROT, X, Y, Z)

	fun move(player: Player, targetLocation: Location, yawOffset: Float = 0.0f, offsetPos: Vector? = null) {
		val serverPlayer = (player as CraftPlayer).handle
		val connection = serverPlayer.connection

		serverPlayer.setPos(targetLocation.x, targetLocation.y, targetLocation.z)
		serverPlayer.setRot(serverPlayer.yRot + yawOffset, serverPlayer.xRot)

		connection.send(ClientboundPlayerPositionPacket(
			offsetPos?.x ?: targetLocation.x,
			offsetPos?.y ?: targetLocation.y,
			offsetPos?.z ?: targetLocation.z,
			yawOffset,
			0f,
			if (offsetPos == null) OFFSET_DIRECTION else OFFSET_ALL,
			0,
			true
		))
	}

	fun teleport(player: Player, loc: Location) {
		move(player, loc, 0.0f)
	}

	fun teleportRotate(player: Player, loc: Location, theta: Float) {
		move(player, loc, theta)
	}

	fun move(player: Player, loc: Location, dx: Double, dy: Double, dz: Double) {
		move(player, loc, 0.0f, Vector(dx, dy, dz))
	}
}
