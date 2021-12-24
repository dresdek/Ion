package net.starlegacy.util

import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.X
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.X_ROT
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Y
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket.RelativeArgument.Z
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl
import net.minecraft.world.phys.Vec3
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.util.Vector
import java.lang.reflect.Field

object ConnectionUtils {
	private val OFFSET_DIRECTION = setOf(X_ROT, Y_ROT)
	private val OFFSET_ALL = setOf(X_ROT, Y_ROT, X, Y, Z)

	private var justTeleportedField: Field = getField("justTeleported") // I do not know the obfuscated name of this field, lets hope this just works
	private var teleportPosField: Field = getField("y") // awaitingPositionFromClient / teleportPos
	private var lastPosXField: Field = getField("o") // lastPosX / lastGoodX
	private var lastPosYField: Field = getField("u") // lastPosY / lastGoodY
	private var lastPosZField: Field = getField("q") // lastPosZ / lastGoodZ
	private var teleportAwaitField: Field = getField("z") // awaitingTeleport / teleportAwait
	private var AField: Field = getField("A")
	private var eField: Field = getField("e")

	@Throws(NoSuchFieldException::class)
	private fun getField(name: String): Field {
		val field = ServerGamePacketListenerImpl::class.java.getDeclaredField(name)
		field.isAccessible = true
		return field
	}

	fun move(player: Player, loc: Location, theta: Float = 0.0f, offsetPos: Vector? = null) {
		val handle = (player as CraftPlayer).handle
		val connection = handle.connection
		val x = loc.x
		val y = loc.y
		val z = loc.z

		if (handle.containerMenu !== handle.inventoryMenu) {
			handle.closeContainer()
		}

		var teleportAwait: Int
		justTeleportedField.set(connection, true)
		teleportPosField.set(connection, Vec3(x, y, z))
		lastPosXField.set(connection, x)
		lastPosYField.set(connection, y)
		lastPosZField.set(connection, z)
		teleportAwait = teleportAwaitField.getInt(connection).plus(1)

		if (teleportAwait == 2147483647) {
			teleportAwait = 0
		}

		teleportAwaitField.set(connection, teleportAwait)
		AField.set(connection, eField.get(connection))

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

//		handle.level.updateChunkPos(handle) // Removed with 1.17.1

		val flags = if (offsetPos != null) OFFSET_ALL else OFFSET_DIRECTION
		val packet = ClientboundPlayerPositionPacket(px, py, pz, theta, 0f, flags, 0, true)
//		old:	ClientboundPlayerPositionPacket(px, py, pz, theta, 0f, flags, teleportAwait)
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

	fun isTeleporting(player: ServerPlayer?): Boolean {
		if (player == null) return false
		return try {
			teleportPosField.get(player.connection) != null
		} catch (e: IllegalAccessException) {
			false
		}
	}

	fun unfreeze(player: Player?) {
		if (player == null || !player.isOnline) return
		if (Bukkit.isPrimaryThread()) move(player, player.location, 0.0, 0.0, 0.0)
		val handle = (player as CraftPlayer).handle
		while (player.isOnline && teleportPosField.get(handle.connection) != null) Thread.sleep(0)
	}
}
