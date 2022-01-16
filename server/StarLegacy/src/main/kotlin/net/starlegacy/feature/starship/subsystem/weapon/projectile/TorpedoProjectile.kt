package net.starlegacy.feature.starship.subsystem.weapon.projectile

import net.horizonsend.ion.server.listeners.commands.QuickBalance
import net.starlegacy.feature.starship.active.ActiveStarship
import net.starlegacy.util.mcName
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class TorpedoProjectile(
	starship: ActiveStarship?,
	loc: Location,
	dir: Vector,
	shooter: Player?,
	originalTarget: Vector,
	baseAimDistance: Int
) : TrackingLaserProjectile(starship, loc, dir, shooter, originalTarget, baseAimDistance) {
	override val range get() = QuickBalance.getBalancedValue("TorpedoProjectileRange")
	override val speed get() = QuickBalance.getBalancedValue("TorpedoProjectileSpeed")
	override val shieldDamageMultiplier get() = QuickBalance.getBalancedValue("TorpedoProjectileshieldDamageMultiplier").toInt()
	override val color: Color = Color.fromRGB(255, 0, 255)
	override val thickness get() = QuickBalance.getBalancedValue("TorpedoProjectileThickness")
	override val particleThickness get() = QuickBalance.getBalancedValue("TorpedoProjectileParticleThickness")
	override val explosionPower get() = QuickBalance.getBalancedValue("TorpedoProjectileExplosionPower").toFloat()
	override val maxDegrees: Double = 45.0
	override val volume: Int = 10
	override val pitch: Float = 0.75f
	override val soundName: String = Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST_FAR.mcName
}