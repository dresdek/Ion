package net.horizonsend.quickbalance

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.horizonsend.Ion.Companion.pluginInstance
import java.io.File

object QuickBalanceManager {
	private val balancedDefaultValues = mutableMapOf<String, Double>(

	)

	private var balanceCustomValues = mutableMapOf<String, Double>()

	val balancedValues get() = balancedDefaultValues.toMutableMap().also { it.putAll(balanceCustomValues) }
	val balancedValueNames get() = balancedValues.keys

	fun getBalancedValue(name: String) = balancedValues[name]
	fun setBalancedValue(name: String, value: Double) {
		balanceCustomValues[name] = value
		saveQuickBalanceValues()
	}

	init {
		try {
			balanceCustomValues = Json.decodeFromStream(File(pluginInstance.dataFolder, "balancedValues.json").inputStream())
		}
		catch (e: Exception) {
			pluginInstance.log4JLogger.warn("balancedValues.json is corrupted or does not exist. Creating a new one.")
			saveQuickBalanceValues()
		}
	}

	private fun saveQuickBalanceValues() {
		Json.encodeToStream(balanceCustomValues, File(pluginInstance.dataFolder, "balancedValues.json").outputStream())
	}
}