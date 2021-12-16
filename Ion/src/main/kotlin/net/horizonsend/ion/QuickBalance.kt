package net.horizonsend.ion

import co.aikar.commands.BaseCommand
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.starlegacy.PLUGIN
import java.io.File

@ExperimentalSerializationApi
object QuickBalance: BaseCommand() {
	private val defaultBalancedValues = mapOf<String, Double>(

	)

	private var customBalancedValues = mutableMapOf<String, Double> ()

	// This is dumb... this will cause us to spend a lot of time moving around data for no reason
	val balancedValues get() = defaultBalancedValues.toMutableMap().apply { putAll(customBalancedValues) }.toMap()

	fun getBalancedValue(name: String) = balancedValues[name]
	fun setBalancedValue(name: String, value: Double) {
		if (!defaultBalancedValues.containsKey(name)) return // If the value does not exist, don't set it.

		customBalancedValues[name] = value
		saveBalancedValues()
	}

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
}