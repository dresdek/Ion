package net.starlegacy.feature.multiblock.starshipweapon.turret

import java.util.concurrent.TimeUnit
import net.horizonsend.ion.server.listeners.commands.QuickBalance
import net.starlegacy.feature.multiblock.MultiblockShape
import net.starlegacy.feature.starship.active.ActiveStarship
import net.starlegacy.feature.starship.subsystem.weapon.TurretWeaponSubsystem
import net.starlegacy.feature.starship.subsystem.weapon.primary.LightTurretWeaponSubsystem
import net.starlegacy.util.Vec3i
import org.bukkit.block.BlockFace

sealed class LightTurretMultiblock : TurretMultiblock() {
	override fun createSubsystem(starship: ActiveStarship, pos: Vec3i, face: BlockFace): TurretWeaponSubsystem {
		return LightTurretWeaponSubsystem(starship, pos, getFacing(pos, starship), this)
	}

	protected abstract fun getSign(): Int

	override val cooldownNanos get() = TimeUnit.SECONDS.toNanos(
		QuickBalance.getBalancedValue("LightTurretCooldownSeconds").toLong())
	override val range get() = QuickBalance.getBalancedValue("LightTurretRange")
	override val sound: String = "starship.weapon.turbolaser.light.shoot"

	override val projectileSpeed get() = QuickBalance.getBalancedValue("LightTurretProjectileSpeed").toInt()
	override val projectileParticleThickness get() = QuickBalance.getBalancedValue("LightTurretProjectileThickness")
	override val projectileExplosionPower get() = QuickBalance.getBalancedValue("LightTurretExplosionPower").toFloat()
	override val projectileShieldDamageMultiplier get() = QuickBalance.getBalancedValue("LightTurretShieldDamageMultiplier")
		.toInt()

	override fun buildFirePointOffsets(): List<Vec3i> = listOf(Vec3i(0, +3 * getSign(), +4))

	override fun MultiblockShape.buildStructure() {
		z(-1) {
			y(getSign() * 3) {
				x(-1).anyStairs()
				x(+0).stainedTerracotta()
				x(+1).anyStairs()
			}
			y(getSign() * 4) {
				x(+0).anyStairs()
			}
		}
		z(+0) {
			y(getSign() * 2) {
				x(+0).sponge()
			}
			y(getSign() * 3) {
				x(-1).stainedTerracotta()
				x(+0).carbyne()
				x(+1).stainedTerracotta()
			}
			y(getSign() * 4) {
				x(-1).anyStairs()
				x(+0).ironBlock()
				x(+1).anyStairs()
			}
		}
		z(+1) {
			y(getSign() * 3) {
				x(-1).anyStairs()
				x(+0).anyGlass()
				x(+1).anyStairs()
			}
			y(getSign() * 4) {
				x(+0).anyGlass()
			}
		}
		z(+2) {
			y(getSign() * 3) {
				x(+0).stainedGlass()
			}
		}
		z(+3) {
			y(getSign() * 3) {
				x(+0).stainedGlass()
			}
		}
	}
}

object TopLightTurretMultiblock : LightTurretMultiblock() {
	override fun getSign(): Int = 1
	override fun getPilotOffset(): Vec3i = Vec3i(+0, +3, +1)
}

object BottomLightTurretMultiblock : LightTurretMultiblock() {
	override fun getSign(): Int = -1
	override fun getPilotOffset(): Vec3i = Vec3i(+0, -4, +1)
}
