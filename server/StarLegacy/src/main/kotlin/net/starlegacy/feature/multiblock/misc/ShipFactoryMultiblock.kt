package net.starlegacy.feature.multiblock.misc

import net.starlegacy.feature.multiblock.Multiblock
import net.starlegacy.feature.multiblock.MultiblockShape
import net.starlegacy.util.getFacing
import net.starlegacy.util.rightFace
import org.bukkit.block.Sign
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

object ShipFactoryMultiblock : Multiblock() {
	override val name = "shipfactory"

	override val signText = createSignText(
		line1 = "&1Ship Factory",
		line2 = null,
		line3 = null,
		line4 = null
	)

	override fun onTransformSign(player: Player, sign: Sign) {
		sign.setLine(2, sign.getLine(1))
		sign.setLine(1, player.uniqueId.toString())
	}

	override fun MultiblockShape.buildStructure() {
		z(+0) {
			y(-1) {
				x(+0).ironBlock()
				x(+1).ironBlock()
			}

			y(+0) {
				x(+0).furnace()
				x(+1).anyPipedInventory()
			}
		}
	}

	fun getStorage(sign: Sign): Inventory {
		val direction = sign.getFacing().oppositeFace
		return (sign.block.getRelative(direction).getRelative(direction.rightFace).state as InventoryHolder).inventory
	}
}
