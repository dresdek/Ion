package net.starlegacy.feature.starship.subsystem.weapon.projectile

import net.horizonsend.ion.server.commands.QuickBalance
import net.starlegacy.feature.starship.active.ActiveStarship
import net.starlegacy.util.mcName
import org.bukkit.Color
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class PointDefenseLaserProjectile(
	starship: ActiveStarship?,
	loc: Location,
	dir: Vector,
	override val range: Double,
	shooter: Player?
) : LaserProjectile(starship, loc, dir, shooter) {
	override val speed get() = QuickBalance.getBalancedValue("PointDefenseSpeed")
	override val shieldDamageMultiplier get() = QuickBalance.getBalancedValue("PointDefenseShieldDamageMultiplier").toInt()
	override val color: Color = Color.BLUE
	override val thickness get() = QuickBalance.getBalancedValue("PointDefenseThickness")
	override val particleThickness get() = QuickBalance.getBalancedValue("PointDefenseParticleThickness")
	override val explosionPower get() = QuickBalance.getBalancedValue("PointDefenseExplosionPower").toFloat()
	override val volume: Int = 20
	override val pitch: Float = 2.0f
	override val soundName: String = Sound.ENTITY_FIREWORK_ROCKET_LARGE_BLAST.mcName
}
