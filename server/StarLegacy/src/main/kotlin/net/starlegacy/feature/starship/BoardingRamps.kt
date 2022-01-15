package net.starlegacy.feature.starship

import net.kyori.adventure.text.Component
import net.starlegacy.feature.starship.active.ActiveStarships
import net.starlegacy.util.colorize
import net.starlegacy.util.msg
import org.bukkit.block.Sign
import org.bukkit.entity.Player

object BoardingRamps {
	val FIRST_LINE = Component.text("&cBoarding Ramp".colorize()) // use Color class later
	val SECOND_LINE_OPEN = Component.text("{&cOPEN&0}".colorize())
	val SECOND_LINE_SHUT = Component.text("{&aSHUT&0}".colorize())

	fun toggle(player: Player, sign: Sign) {
		when (sign.line(1)) {
			SECOND_LINE_OPEN -> shut(player, sign)
			SECOND_LINE_SHUT -> open(player, sign)
			else -> sign.block.breakNaturally()
		}
	}

	fun open(player: Player, sign: Sign) {
		if (isPartOfActivatedStarship(sign)) {
			player msg "&cYou can't toggle the boarding ramp of an activated starship!"
			return
		}

		if (isOutside(player)) {
			player msg "&cYou can't toggle a boarding ramp from outside!"
			return
		}

		BoardingRampUtils.openRamp(sign)
	}

	fun shut(player: Player, sign: Sign): Boolean {
		if (isPartOfActivatedStarship(sign)) {
			player msg "&cYou can't toggle the boarding ramp of an activated starship!"
			return false
		}

		if (isOutside(player)) {
			player msg "&cYou can't toggle a boarding ramp from outside!"
			return false
		}

		return BoardingRampUtils.closeRamp(sign, player)
	}

	private fun isPartOfActivatedStarship(sign: Sign): Boolean {
		return ActiveStarships.findByBlock(sign.block) != null
	}

	private fun isOutside(player: Player): Boolean {
		val pitch = player.location.pitch
		return pitch < 0
	}
}
