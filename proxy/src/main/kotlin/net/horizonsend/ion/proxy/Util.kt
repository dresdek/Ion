package net.horizonsend.ion.proxy

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.Player
import java.io.File
import java.lang.System.currentTimeMillis
import java.net.URL
import java.util.UUID
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import net.horizonsend.ion.proxy.Ion.Companion.plugin
import net.horizonsend.ion.proxy.data.BanData
import net.horizonsend.ion.proxy.data.UUIDData
import net.horizonsend.ion.proxy.data.UsernameData
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text

val SERVER_UUID: UUID = UUID.fromString("00000000-0000-0000-0000-000000000000")

val proxy get() = plugin.server
val data get() = plugin.dataDirectory

val banDataDirectory get() = File(data.toFile(), "banData").apply { mkdir() }

fun getNameFromUUID(uuid: UUID): String {
	return when(uuid) {
		SERVER_UUID -> "Server"
		else -> {
			URL("https://api.mojang.com/user/profiles/$uuid/names").openStream().use {
				val names = Json.decodeFromStream<List<UsernameData>>(it)
				names.last().name
			}
		}
	}
}

fun getUUIDFromName(name: String): UUID {
	return when(name) {
		"Server" -> SERVER_UUID
		else -> {
			URL("https://api.mojang.com/users/profiles/minecraft/$name").openStream().use {
				UUID.fromString(Json.decodeFromStream<UUIDData>(it).id)
			}
		}
	}
}

fun targetsFromIonSelector(selector: String): Set<Player> =
	if (selector == "*") proxy.allPlayers.toSet()
	else if (selector.startsWith("@")) {
		val targetServer = proxy.getServer(selector.substring(1)).orElse(null)
		targetServer?.playersConnected?.toSet() ?: emptySet()
	} else {
		val targetPlayer = proxy.getPlayer(selector).orElse(null)
		if (targetPlayer != null) setOf(targetPlayer) else emptySet()
	}

fun constructBanMessage(issuerName: String, reason: String, expires: Long = 0L): Component {
	val expiresIn = (expires - currentTimeMillis()).milliseconds

	val expiresInString = if (expires == 0L) "Never" else when {
		expiresIn.inWholeDays > 0 -> "in ${expiresIn.inWholeDays} days"
		expiresIn.inWholeHours > 0 -> "in ${expiresIn.inWholeHours} hours"
		expiresIn.inWholeMinutes > 0 -> "in ${expiresIn.inWholeMinutes} minutes"
		expiresIn.inWholeSeconds > 0 -> "in ${expiresIn.inWholeSeconds} seconds"
		else -> "now"
	}

	return text("You were banned by $issuerName!\n\nReason: $reason\nExpires: $expiresInString")
}

fun banPlayer(issuer: CommandSource, uuid: UUID, reason: String, expires: Long = 0L) {
	val targetPlayer = proxy.getPlayer(uuid).orElse(null)

	targetPlayer?.disconnect(constructBanMessage(issuer.ensuredName, reason, expires))

	File(banDataDirectory, "$uuid.json").apply{ createNewFile() }.writeText(Json.encodeToString(BanData(issuer.ensuredUUID.toString(), reason, expires)))
}