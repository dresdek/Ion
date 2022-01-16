package net.starlegacy.feature.misc

import net.horizonsend.ion.server.listeners.commands.QuickBalance
import kotlin.math.max
import net.starlegacy.PLUGIN
import net.starlegacy.SLComponent
import net.starlegacy.feature.multiblock.Multiblocks
import net.starlegacy.feature.multiblock.misc.DecomposerMultiblock
import net.starlegacy.util.getFacing
import net.starlegacy.util.getRelativeIfLoaded
import net.starlegacy.util.msg
import net.starlegacy.util.rightFace
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.Sign
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

object Decomposers : SLComponent() {
	val busySigns = mutableSetOf<Location>()
//remove const on maxlength  1/1/21
	private val MAX_LENGTH get() = QuickBalance.getBalancedValue("DecomposersMaxLength").toInt()
	private const val BLOCKS_PER_SECOND = 1000
	private val FRAME_MATERIAL = Material.CHISELED_QUARTZ_BLOCK

	@EventHandler
	fun onClick(event: PlayerInteractEvent) {
		if (event.action != Action.RIGHT_CLICK_BLOCK) {
			return
		}

		val sign = event.clickedBlock?.state as? Sign ?: return

		val multiblock = Multiblocks[sign] as? DecomposerMultiblock ?: return

		val signLoc = sign.location

		val forward = sign.getFacing().oppositeFace
		val up = BlockFace.UP
		val right = forward.rightFace

		val origin: Location = signLoc.clone()
			.add(forward.direction.multiply(2))
			.add(up.direction)
			.add(right.direction)

		val frameOrigin: Location = signLoc.clone().add(forward.direction)

		if (!busySigns.add(signLoc)) {
			event.player msg "&cDecomposer in use"
			return
		}

		val width = getDimension(frameOrigin, right)
		val height = getDimension(frameOrigin, up)
		val length = getDimension(frameOrigin, forward)
		val area = height * length
		val delay = max(10L, area / BLOCKS_PER_SECOND * 20L)

		DecomposeTask(
			signLoc,
			width,
			height,
			length,
			origin,
			right,
			up,
			forward,
			event.player.uniqueId,
			multiblock
		).runTaskTimer(PLUGIN, delay, delay)
	}

	private fun getDimension(origin: Location, direction: BlockFace): Int {
		var dimension = 0
		var tempBlock = origin.block

		while (dimension < MAX_LENGTH) {
			tempBlock = tempBlock.getRelativeIfLoaded(direction)
				?: return dimension

			if (tempBlock.type != FRAME_MATERIAL) {
				return dimension
			}

			dimension++
		}

		return dimension
	}
}
