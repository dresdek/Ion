package net.starlegacy.feature.multiblock.starshipweapon.turret

import java.util.concurrent.TimeUnit
import net.horizonsend.ion.server.listeners.commands.QuickBalance.getBalancedValue
import net.starlegacy.feature.multiblock.MultiblockShape
import net.starlegacy.feature.starship.active.ActiveStarship
import net.starlegacy.feature.starship.subsystem.weapon.TurretWeaponSubsystem
import net.starlegacy.feature.starship.subsystem.weapon.primary.HeavyTurretWeaponSubsystem
import net.starlegacy.util.Vec3i
import org.bukkit.block.BlockFace

sealed class HeavyTurretMultiblock : TurretMultiblock() {
	override fun createSubsystem(starship: ActiveStarship, pos: Vec3i, face: BlockFace): TurretWeaponSubsystem {
		return HeavyTurretWeaponSubsystem(starship, pos, getFacing(pos, starship), this)
	}

	protected abstract fun getSign(): Int

	override val cooldownNanos get() = TimeUnit.SECONDS.toNanos(getBalancedValue("HeavyTurretCooldownSeconds").toLong())
	override val range get() = getBalancedValue("HeavyTurretRange")
	override val sound: String = "starship.weapon.turbolaser.heavy.shoot"

	override val projectileSpeed get() = getBalancedValue("HeavyTurretProjectileSpeed").toInt()
	override val projectileParticleThickness get() = getBalancedValue("HeavyTurretProjectileThickness")
	override val projectileExplosionPower get() = getBalancedValue("HeavyTurretExplosionPower").toFloat()
	override val projectileShieldDamageMultiplier get() = getBalancedValue("HeavyTurretShieldDamageMultiplier").toInt()

	override fun buildFirePointOffsets(): List<Vec3i> =
		listOf(Vec3i(-1, getSign() * 3, +4), Vec3i(1, getSign() * 3, +4))

	override fun MultiblockShape.buildStructure() {
		z(-2) {
			y(getSign() * 3) {
				x(-1).anyStairs()
				x(+0).anyStairs()
				x(+1).anyStairs()
			}
		}
		z(-1) {
			y(getSign() * 2) {
				x(+0).sponge()
			}
			y(getSign() * 3) {
				x(-2).anyStairs()
				x(-1).carbyne()
				x(+0).carbyne()
				x(+1).carbyne()
				x(+2).anyStairs()
			}
			y(getSign() * 4) {
				x(-1).anyStairs()
				x(+0).stainedTerracotta()
				x(+1).anyStairs()
			}
		}
		z(+0) {
			y(getSign() * 2) {
				x(-1).sponge()
				x(+1).sponge()
			}
			y(getSign() * 3) {
				x(-2).anyStairs()
				x(-1).carbyne()
				x(+0).carbyne()
				x(+1).carbyne()
				x(+2).anyStairs()
			}
			y(getSign() * 4) {
				x(-1).stainedTerracotta()
				x(+0).ironBlock()
				x(+1).stainedTerracotta()
			}
		}
		z(+1) {
			y(getSign() * 2) {
				x(+0).sponge()
			}
			y(getSign() * 3) {
				x(-2).anyStairs()
				x(-1).carbyne()
				x(+0).anyGlass()
				x(+1).carbyne()
				x(+2).anyStairs()
			}
			y(getSign() * 4) {
				x(-1).anyStairs()
				x(+0).anyGlass()
				x(+1).anyStairs()
			}
		}
		z(+2) {
			y(getSign() * 3) {
				x(-1).stainedGlass()
				x(+1).stainedGlass()
			}
		}
		z(+3) {
			y(getSign() * 3) {
				x(-1).stainedGlass()
				x(+1).stainedGlass()
			}
		}
	}
}

object TopHeavyTurretMultiblock : HeavyTurretMultiblock() {
	override fun getSign(): Int = 1
	override fun getPilotOffset(): Vec3i = Vec3i(+0, +3, +1)
}

object BottomHeavyTurretMultiblock : HeavyTurretMultiblock() {
	override fun getSign(): Int = -1
	override fun getPilotOffset(): Vec3i = Vec3i(+0, -4, +1)
}
