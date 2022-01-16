package net.starlegacy.feature.starship.subsystem.weapon.secondary

import net.horizonsend.ion.server.listeners.commands.QuickBalance.getBalancedValue
import java.util.concurrent.TimeUnit
import net.starlegacy.feature.starship.active.ActiveStarship
import net.starlegacy.feature.starship.subsystem.weapon.TargetTrackingCannonWeaponSubsystem
import net.starlegacy.feature.starship.subsystem.weapon.interfaces.HeavyWeaponSubsystem
import net.starlegacy.feature.starship.subsystem.weapon.projectile.TorpedoProjectile
import net.starlegacy.util.Vec3i
import org.bukkit.Location
import org.bukkit.block.BlockFace
import org.bukkit.entity.Player
import org.bukkit.util.Vector

class TorpedoWeaponSubsystem(
	starship: ActiveStarship,
	pos: Vec3i,
	face: BlockFace
) : TargetTrackingCannonWeaponSubsystem(starship, pos, face),
	HeavyWeaponSubsystem {
	override val boostChargeNanos get() = TimeUnit.SECONDS.toNanos(getBalancedValue("TorpedoBoostChargeNanos").toLong())

	override fun isForwardOnly(): Boolean = true

	override val length: Int = 3
	override val powerUsage get() = getBalancedValue("TorpedoPowerUsage").toInt()
	override val extraDistance: Int = 1
	override val aimDistance: Int = 3

	override fun getMaxPerShot() = getBalancedValue("MaxTorpedoPerShot").toInt()

	override fun fire(loc: Location, dir: Vector, shooter: Player, target: Vector?) {
		TorpedoProjectile(starship, loc, dir, shooter, checkNotNull(target), aimDistance).fire()
	}
}