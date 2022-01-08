package net.starlegacy.feature.starship.subsystem.weapon.primary

import net.horizonsend.ion.server.commands.QuickBalance
import net.starlegacy.feature.multiblock.starshipweapon.turret.LightTurretMultiblock
import net.starlegacy.feature.starship.active.ActiveStarship
import net.starlegacy.feature.starship.subsystem.weapon.TurretWeaponSubsystem
import net.starlegacy.util.Vec3i
import org.bukkit.block.BlockFace

class LightTurretWeaponSubsystem(
	ship: ActiveStarship,
	pos: Vec3i,
	face: BlockFace,
	override val multiblock: LightTurretMultiblock
) : TurretWeaponSubsystem(ship, pos, face) {
	override val inaccuracyRadians get() = Math.toRadians(QuickBalance.getBalancedValue("LightTurretInaccuracy"))
	override val powerUsage get() = QuickBalance.getBalancedValue("LightTurretPowerUsage").toInt()
}
