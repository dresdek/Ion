package net.starlegacy.util

import java.util.UUID
import net.starlegacy.SLComponent
import net.starlegacy.cache.nations.PlayerCache
import net.starlegacy.database.Oid
import net.starlegacy.database.schema.nations.Nation
import net.starlegacy.database.schema.nations.Settlement
import org.bukkit.Bukkit
import org.litote.kmongo.id.WrappedObjectId

object Notify : SLComponent() {
	infix fun online(message: String) = notifyOnlineAction(message.colorize())
	private val notifyOnlineAction = { message: String ->
		Bukkit.broadcastMessage(message)
	}.registerRedisAction("notify-online", runSync = false)

	infix fun all(message: String) {
		val colorized = message.colorize()
		online(colorized)
	}

	fun player(player: UUID, message: String) {
		notifyPlayerAction(player to message.colorize())
	}

	private val notifyPlayerAction = { (uuid, message): Pair<UUID, String> ->
		Bukkit.getPlayer(uuid)?.sendMessage(message)
	}.registerRedisAction("notify-player", runSync = false)

	fun settlement(settlementId: Oid<Settlement>, message: String) {
		notifySettlementAction(settlementId.toString() to "&3$message".colorize())
	}

	private val notifySettlementAction = { (idString, message): Pair<String, String> ->
		val id: Oid<Settlement> = WrappedObjectId(idString)
		Bukkit.getOnlinePlayers()
			.filter { PlayerCache[it].settlement == id }
			.forEach { it.sendMessage(message) }
	}.registerRedisAction("notify-settlement", runSync = false)

	fun nation(nationId: Oid<Nation>, message: String) {
		notifyNationAction(nationId.toString() to "&6$message".colorize())
	}

	private val notifyNationAction = { (idString, message): Pair<String, String> ->
		val id: Oid<Nation> = WrappedObjectId(idString)
		Bukkit.getOnlinePlayers()
			.filter { PlayerCache[it].nation == id }
			.forEach { it.sendMessage(message) }
	}.registerRedisAction("notify-nation", runSync = false)

	override fun supportsVanilla(): Boolean {
		return true
	}
}
