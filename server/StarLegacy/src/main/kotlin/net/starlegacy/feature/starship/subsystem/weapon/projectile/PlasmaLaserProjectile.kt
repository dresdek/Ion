package net.starlegacy.feature.starship.subsystem.weapon.projectile

import net.horizonsend.ion.server.commands.QuickBalance
import net.starlegacy.feature.starship.active.ActiveStarship
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class PlasmaLaserProjectile(
	starship: ActiveStarship,
	loc: Location,
	dir: Vector,
	shooter: Player?
) : LaserProjectile(starship, loc, dir, shooter) {
	override val range get() = QuickBalance.getBalancedValue("PlasmaLaserProjectileRange")
	override val speed get() = QuickBalance.getBalancedValue("PlasmaLaserProjectileSpeed")
	override val shieldDamageMultiplier get() = QuickBalance.getBalancedValue("PlasmaLaserProjectileShieldDamageMultiplier").toInt()
	override val color: Color = starship.weaponColor
	override val thickness get() = QuickBalance.getBalancedValue("PlasmaLaserProjectileThickness")
	override val particleThickness get() = QuickBalance.getBalancedValue("PlasmaLaserProjectileParticleThickness")
	override val explosionPower get() = QuickBalance.getBalancedValue("PlasmaLaserProjectileExplosionPower").toFloat()
	override val volume get() = QuickBalance.getBalancedValue("PlasmaLaserProjectileVolume").toInt()
	override val soundName: String = "starship.weapon.plasma_cannon.shoot"
}