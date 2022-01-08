package net.horizonsend.ion.server.commands.starships

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import net.horizonsend.ion.server.niceTimeFormatting
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.starlegacy.database.MongoManager
import net.starlegacy.database.schema.starships.PlayerStarshipData
import net.starlegacy.database.slPlayerId
import net.starlegacy.util.blockKeyX
import net.starlegacy.util.blockKeyY
import net.starlegacy.util.blockKeyZ
import org.bukkit.entity.Player
import org.litote.kmongo.eq

@CommandAlias("starships")
@Suppress("unused")
object Starships: BaseCommand() {
	@Default
	@Description("Lists all starships you own.")
	@CommandAlias("starships")
	fun starships(sender: Player) {
		val component = Component.text()
		component.append(Component.text("Your Starships:").decorate(TextDecoration.BOLD))

		MongoManager.getCollection(PlayerStarshipData::class).find(PlayerStarshipData::captain eq sender.slPlayerId).forEach {
			component.append(Component.text("\n"))
			if (it.isLockActive()) component.append(Component.text("Locked ", NamedTextColor.RED))
			component.append(Component.text(it.type.displayName, NamedTextColor.AQUA))
			component.append(Component.text(" on ", NamedTextColor.GRAY))
			component.append(Component.text(it.world, NamedTextColor.GOLD))
			component.append(Component.text(" at ", NamedTextColor.GRAY))
			component.append(Component.text("${blockKeyX(it.blockKey)}, ${blockKeyY(it.blockKey)}, ${blockKeyZ(it.blockKey)} ",
				NamedTextColor.LIGHT_PURPLE))
			component.append(Component.text("last used ", NamedTextColor.GRAY))
			component.append(Component.text(niceTimeFormatting(System.currentTimeMillis() - it.lastUsed),
				NamedTextColor.YELLOW))
			component.append(Component.text(" named ", NamedTextColor.GRAY))
			component.append(Component.text(it.name ?: "[nothing]", NamedTextColor.GREEN))
		}

		sender.sendMessage(component)
	}
}