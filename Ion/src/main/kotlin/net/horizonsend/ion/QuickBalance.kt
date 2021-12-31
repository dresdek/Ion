package net.horizonsend.ion

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import java.io.File
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.horizonsend.ion.Ion.Companion.plugin
import org.bukkit.command.CommandSender

@CommandPermission("ion.quickbalance")
@CommandAlias("quickbalance")
object QuickBalance: BaseCommand() {
	private val defaultBalancedValues = mapOf(
		"TriTurretCooldownSeconds" to 3.0,
		"TriTurretInaccuracy" to 3.0,
		"TriTurretPowerUsage" to 45000.0,
		"TriTurretRange" to 500.0,
		"TriTurretProjectileSpeed" to 125.0,
		"TriTurretExplosionPower" to 6.0,
		"TriTurretShieldDamageMultiplier" to 3.0,
	)

	private var customBalancedValues = mutableMapOf<String, Double> ()

	// This is dumb... this will cause us to spend a lot of time moving around data for no reason
	val balancedValues get() = defaultBalancedValues.toMutableMap().apply { putAll(customBalancedValues) }.toMap()

	fun getBalancedValue(name: String) = balancedValues[name] ?: throw IllegalArgumentException("No balanced value for $name")

	init {
		try {
			customBalancedValues = Json.decodeFromStream(File(plugin.dataFolder, "values.json").inputStream())
		} catch (e: Exception) {
			plugin.logger.warning("Failed to load custom balanced values. Creating new file.")
			saveBalancedValues()
		}
	}

	private fun saveBalancedValues() {
		Json.encodeToStream(customBalancedValues, File(plugin.dataFolder, "values.json").outputStream())
	}

	@Subcommand("list")
	fun list(sender: CommandSender): Boolean {
		sender.sendMessage("QuickBalance Values:\n" + balancedValues.map{"${it.key.padEnd(balancedValues.keys.sortedBy{it.length}.size)} = ${it.value}"}.joinToString("\n"))
		return true
	}

	@Subcommand("get")
	@CommandCompletion("@valueNames")
	fun get(sender: CommandSender, name: String): Boolean {
		if (!balancedValues.containsKey(name)) {
			sender.sendMessage("Balance value $name does not exist.")
			return false
		}

		sender.sendMessage("$name = ${balancedValues[name]}")
		return true
	}

	@Subcommand("set")
	@CommandCompletion("@valueNames")
	fun set(sender: CommandSender, name: String, value: Double): Boolean {
		if (!balancedValues.containsKey(name)) {
			sender.sendMessage("Balance value $name does not exist.")
			return false
		}

		customBalancedValues[name] = value
		saveBalancedValues()

		sender.sendMessage("$name has been set to ${balancedValues[name]}")

		return true
	}

	@Subcommand("clear")
	@CommandCompletion("@valueNames")
	fun clear(sender: CommandSender, name: String): Boolean {
		customBalancedValues.remove(name)
		saveBalancedValues()

		sender.sendMessage("$name has been cleared")

		return true
	}
}