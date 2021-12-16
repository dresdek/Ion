package net.horizonsend.ion

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Subcommand
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.starlegacy.PLUGIN
import org.bukkit.command.CommandSender
import java.io.File

@ExperimentalSerializationApi
@CommandAlias("quickbalance")
object QuickBalance: BaseCommand() {
	private val defaultBalancedValues = mapOf(
		"test" to 1.2
	)

	private var customBalancedValues = mutableMapOf<String, Double> ()

	// This is dumb... this will cause us to spend a lot of time moving around data for no reason
	private val balancedValues get() = defaultBalancedValues.toMutableMap().apply { putAll(customBalancedValues) }.toMap()

	init {
		try {
			customBalancedValues = Json.decodeFromStream(File(PLUGIN.dataFolder, "values.json").inputStream())
		} catch (e: Exception) {
			PLUGIN.logger.warning("Failed to load custom balanced values. Creating new file.")
			saveBalancedValues()
		}
	}

	private fun saveBalancedValues() {
		Json.encodeToStream(customBalancedValues, File(PLUGIN.dataFolder, "values.json").outputStream())
	}

	@Subcommand("list")
	fun list(sender: CommandSender): Boolean {
		sender.sendMessage("QuickBalance Values:\n" + balancedValues.map {"${it.key.padEnd(balancedValues.keys.sortedBy{it.length}.size)} = ${it.value}\n"})
		return true
	}

	@Subcommand("get")
	fun get(sender: CommandSender, name: String): Boolean {
		if (!balancedValues.containsKey(name)) {
			sender.sendMessage("Balance value $name does not exist.")
			return false
		}

		sender.sendMessage("$name = ${balancedValues[name]}")
		return true
	}

	@Subcommand("set")
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
}