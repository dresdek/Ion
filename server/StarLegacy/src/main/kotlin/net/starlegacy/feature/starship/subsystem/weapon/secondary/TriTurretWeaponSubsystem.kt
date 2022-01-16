package net.starlegacy.feature.starship.subsystem.weapon.secondary

import net.horizonsend.ion.server.listeners.commands.QuickBalance
import net.starlegacy.feature.multiblock.starshipweapon.turret.TriTurretMultiblock
import net.starlegacy.feature.starship.active.ActiveStarship
import net.starlegacy.feature.starship.subsystem.weapon.TurretWeaponSubsystem
import net.starlegacy.feature.starship.subsystem.weapon.interfaces.HeavyWeaponSubsystem
import net.starlegacy.util.Vec3i
import org.bukkit.block.BlockFace

class TriTurretWeaponSubsystem(
	ship: ActiveStarship,
	pos: Vec3i,
	face: BlockFace,
	override val multiblock: TriTurretMultiblock
) : TurretWeaponSubsystem(ship, pos, face),
	HeavyWeaponSubsystem {
	override val inaccuracyRadians get() = Math.toRadians(QuickBalance.getBalancedValue("TriTurretInaccuracy"))
	override val powerUsage get() = QuickBalance.getBalancedValue("TriTurretPowerUsage").toInt()
	override val boostChargeNanos get() = multiblock.cooldownNanos
}
