package net.horizonsend.ion.server

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandCompletion
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Subcommand
import java.io.File
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.horizonsend.ion.server.Ion.Companion.ionInstance
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
		"TriTurretProjectileThickness" to 0.8,
		"HeavyTurretCooldownSeconds" to 0.5,
		"HeavyTurretInaccuracy" to 2.5,
		"HeavyTurretPowerUsage" to 8000.0,
		"HeavyTurretRange" to 300.0,
		"HeavyTurretProjectileSpeed" to 200.0,
		"HeavyTurretExplosionPower" to 4.0,
		"HeavyTurretShieldDamageMultiplier" to 2.0,
		"HeavyTurretProjectileThickness" to 0.5,
		"LightTurretCooldownSeconds" to 0.25,
		"LightTurretInaccuracy" to 2.0,
		"LightTurretPowerUsage" to 6000.0,
		"LightTurretRange" to 200.0,
		"LightTurretProjectileSpeed" to 250.0,
		"LightTurretExplosionPower" to 4.0,
		"LightTurretShieldDamageMultiplier" to 2.0,
		"LightTurretProjectileThickness" to 0.3,
		"AllowPowerModeOvercharging" to 0.0,
		"PowerModeOverchargingPointLimit" to 2000.0,
		"PowerModeOverchargingFailureInterval" to 100.0,
		"TorpedoBoostChargeNanos" to 10.0,
		"TorpedoProjectileRange" to 100.0,
		"TorpedoProjectileSpeed" to 70.0,
		"TorpedoProjectileshieldDamageMultiplier" to 2.0,
		"TorpedoProjectileThickness" to 0.4,
		"TorpedoProjectileParticleThickness" to 1.0,
		"TorpedoProjectileExplosionPower" to  6.0,
		"TorpedoPowerUsage" to 10000.0,
		"MaxTorpedoPerShot" to 5.0,
		"PlasmaCannonPowerUsage" to 2500.0,
		"PlasmaLaserProjectileRange" to 160.0,
		"PlasmaLaserProjectileSpeed" to 400.0,
		"PlasmaLaserProjectileShieldDamageMultiplier" to 3.0,
		"PlasmaLaserProjectileThickness" to 0.3,
		"PlasmaLaserProjectileParticleThickness" to 0.5,
		"PlasmaLaserProjectileExplosionPower" to 4.0,
		"PlasmaLaserProjectileVolume" to 10.0,
        "MaxPlasmaPerShot" to 2.0,
		"PointDefensePowerUsage" to 500.0,
	    "PointDefenseRange" to 120.0,
		"PointDefenseSpeed" to 150.0,
		"PointDefenseShieldDamageMultiplier" to 0.0,
		"PointDefenseThickness" to 0.2,
		"PointDefenseParticleThickness" to 0.35,
		"PointDefenseExplosionPower" to 0.0,
		"HeavyLaserPowerUsage" to 30000.0,
		"HeavyLaserBoostChargeTime" to 5.0,
		"HeavyLaserRequiredAmmo" to 2.0,
		"HeavyLaserShieldDamageMultiplier" to 2.0,
		"HeavyLaserMaxDegrees" to 25.0,
		"HeavyLaserRange" to 200.0,
		"HeavyLaserSpeed" to 50.0,
		"HeavyLaserThickness" to 0.35,
		"HeavyLaserParticleThickness" to 1.0,
		"HeavyLaserExplosionPower" to 12.0,
		"PhaserProjectileRange" to 140.0,
		"PhaserAmmoRequirement" to 4.0,
		"PhaserBoostChargeTime" to 3.0,
		"PhaserProjectileShieldDamageMultiplier" to 55.0,
		"PhaserProjectileThickness" to 0.2,
		"PhaserProjectileExplosionPower" to 2.0,
		//No idea what either of the two below do tbh but speed it hates me when I mess with phaserprojectilespeed
		//so I left it as it was as phasers have infinite spped.
		"PhaserProjectileSpeedUpSpeed" to 1000.0,
		"PhaserProjectileSpeedUpTime" to 500.0,
		"RocketPowerUsage" to 50000.0,
        "RocketRange" to 300.0,
		"RocketSpeed" to 5.0,
		"RocketShieldDamageMultiplier" to 5.0,
		"RocketThickness" to 1.0,
		"RocketExplosionPower" to 30.0,
		"RocketVolume" to 10.0,
		"CruisePulsingSlowDown" to 0.9,
		"CombatNPCDespawnTime" to 1.0,
		"DecomposersMaxLength" to 100.0,
	)

	private var customBalancedValues = mutableMapOf<String, Double> ()

	var balancedValues = defaultBalancedValues.toMutableMap()
		private set

	fun getBalancedValue(name: String) = balancedValues[name] ?: throw IllegalArgumentException("No balanced value for $name")

	init {
		try {
			customBalancedValues = Json.decodeFromStream(File(ionInstance.dataFolder, "values.json").inputStream())
		} catch (e: Exception) {
			ionInstance.log4JLogger.warn("Failed to load custom balanced values. Creating new file.")
			saveBalancedValues()
		}
		updateBalancedValues()
	}

	private fun updateBalancedValues() {
		balancedValues = defaultBalancedValues.toMutableMap().apply { putAll(customBalancedValues) }
	}

	private fun saveBalancedValues() {
		Json.encodeToStream(customBalancedValues, File(ionInstance.dataFolder, "values.json").outputStream())

		updateBalancedValues()
	}

	@Subcommand("list")
	fun list(sender: CommandSender) = sender.sendMessage("QuickBalance Values:\n" + balancedValues.map{"${it.key} = ${it.value}"}.sortedBy{it.length}.joinToString("\n"))

	@Subcommand("get")
	@CommandCompletion("@valueNames")
	fun get(sender: CommandSender, name: String) {
		if (!balancedValues.containsKey(name)) sender.sendMessage("Balance value $name does not exist.")

		sender.sendMessage("$name = ${balancedValues[name]}")
	}

	@Subcommand("set")
	@CommandCompletion("@valueNames 0.0")
	fun set(sender: CommandSender, name: String, value: Double) {
		if (!balancedValues.containsKey(name)) sender.sendMessage("Balance value $name does not exist.")

		customBalancedValues[name] = value
		saveBalancedValues()

		sender.sendMessage("$name has been set to ${balancedValues[name]}")
	}

	@Subcommand("clear")
	@CommandCompletion("@valueNames")
	fun clear(sender: CommandSender, name: String) {
		customBalancedValues.remove(name)
		saveBalancedValues()

		sender.sendMessage("$name has been cleared")
	}
}