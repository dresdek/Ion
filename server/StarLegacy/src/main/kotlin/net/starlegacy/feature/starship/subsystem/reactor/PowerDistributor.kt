package net.starlegacy.feature.starship.subsystem.reactor

class PowerDistributor {
	var shieldPortion: Double = 0.3
		private set
	var weaponPortion: Double = 0.3
		private set
	var thrusterPortion: Double = 0.4
		private set

	fun setDivision(shield: Double, weapon: Double, thruster: Double) {
		this.shieldPortion = shield
		this.weaponPortion = weapon
		this.thrusterPortion = thruster
	}
}
