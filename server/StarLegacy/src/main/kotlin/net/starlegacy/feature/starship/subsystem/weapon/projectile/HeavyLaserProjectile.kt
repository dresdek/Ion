package net.starlegacy.feature.starship.subsystem.weapon.projectile

import net.horizonsend.ion.server.listeners.commands.QuickBalance
import net.starlegacy.feature.starship.active.ActiveStarship
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class HeavyLaserProjectile(
	starship: ActiveStarship?,
	loc: Location,
	dir: Vector,
	shooter: Player?,
	originalTarget: Vector,
	baseAimDistance: Int,
	sound: String
) : TrackingLaserProjectile(starship, loc, dir, shooter, originalTarget, baseAimDistance) {
	override val shieldDamageMultiplier get() = QuickBalance.getBalancedValue("HeavyLaserShieldDamageMultiplier").toInt()
	override val maxDegrees get() = QuickBalance.getBalancedValue("HeavyLaserMaxDegrees")
	override val range get() = QuickBalance.getBalancedValue("HeavyLaserRange")
	override val speed get() = QuickBalance.getBalancedValue("HeavyLaserSpeed")
	override val color: Color = Color.RED
	override val thickness get() = QuickBalance.getBalancedValue("HeavyLaserThickness")
	override val particleThickness get() = QuickBalance.getBalancedValue("HeavyLaserParticleThickness")
	override val explosionPower get() = QuickBalance.getBalancedValue("HeavyLaserExplosionPower").toFloat()
	override val soundName: String = sound
}
