package net.starlegacy.util

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector

object ConnectionUtils {
	fun move(player: Player, loc: Location, theta: Float = 0.0f, offsetPos: Vector? = null) {
		loc.pitch = player.location.pitch
		loc.yaw = player.location.yaw + theta

		player.teleport(loc.add(offsetPos ?: Vector(0.0, 0.0, 0.0)))
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
