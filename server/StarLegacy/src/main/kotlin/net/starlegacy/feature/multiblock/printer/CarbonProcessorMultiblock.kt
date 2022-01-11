package net.starlegacy.feature.multiblock.printer

import net.starlegacy.feature.multiblock.FurnaceMultiblock
import net.starlegacy.feature.multiblock.Multiblock
import net.starlegacy.feature.multiblock.MultiblockShape
import net.starlegacy.util.LegacyItemUtils
import net.starlegacy.util.getFacing
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.block.Furnace
import org.bukkit.block.Sign
import org.bukkit.event.inventory.FurnaceBurnEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

object CarbonProcessorMultiblock : Multiblock(), FurnaceMultiblock {
	override val name = "processor"

	override val signText = createSignText(
		line1 = "&3Carbon",
		line2 = "&7Processor",
		line3 = null,
		line4 = "&7:[''']:"
	)

	override fun MultiblockShape.buildStructure() {
		z(+0) {
			y(-1) {
				x(-1).ironBlock()
				x(+0).ironBlock()
				x(+1).ironBlock()
			}

			y(+0) {
				x(-1).anyGlassPane()
				x(+0).machineFurnace()
				x(+1).anyGlassPane()
			}
		}

		z(+1) {
			y(-1) {
				x(-1).sponge()
				x(+0).goldBlock()
				x(+1).sponge()
			}

			y(+0) {
				x(-1).anyGlass()
				x(+0).stainedGlass()
				x(+1).anyGlass()
			}
		}

		z(+2) {
			y(-1) {
				x(-1).ironBlock()
				x(+0).hopper()
				x(+1).ironBlock()
			}

			y(+0) {
				x(-1).anyGlassPane()
				x(+0).anyPipedInventory()
				x(+1).anyGlassPane()
			}
		}
	}

	fun getOutputBlock(sign: Block): Block {
		return sign.getRelative((sign.getState(false) as Sign).getFacing().oppositeFace, 3)
	}

	fun getOutput(sign: Block): ItemStack {
		val direction: BlockFace = (sign.getState(false) as Sign).getFacing().oppositeFace
		val typeName = sign.getRelative(direction, 2).type.name.replace("STAINED_GLASS", "CONCRETE")
		val type = Material.getMaterial(typeName) ?: error("No material $typeName")
		return ItemStack(type, 1)
	}

	override fun onFurnaceTick(
		event: FurnaceBurnEvent,
		furnace: Furnace,
		sign: Sign
	) {
		event.isCancelled = true
		val smelting = furnace.inventory.smelting
		val fuel = furnace.inventory.fuel
		val inventory = (getOutputBlock(sign.block).getState(false) as InventoryHolder).inventory
		val output = getOutput(sign.block)
		if (!LegacyItemUtils.canFit(inventory, output)) {
			return
		}
		LegacyItemUtils.addToInventory(inventory, output)
		if (fuel != null) {
			fuel.amount = fuel.amount - 1
		}
		event.isBurning = false
		event.burnTime = 50
		furnace.cookTime = (-1000).toShort()
		event.isCancelled = false
	}
}
