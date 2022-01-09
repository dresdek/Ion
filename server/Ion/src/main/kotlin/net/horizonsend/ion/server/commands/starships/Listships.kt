package net.horizonsend.ion.server.commands.starships

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.Description
import co.aikar.commands.annotation.Subcommand
import net.horizonsend.ion.server.niceTimeFormatting
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.starlegacy.database.MongoManager
import net.starlegacy.database.schema.starships.PlayerStarshipData
import net.starlegacy.database.slPlayerId
import net.starlegacy.feature.starship.active.ActivePlayerStarship
import net.starlegacy.feature.starship.active.ActiveStarships
import net.starlegacy.util.blockKeyX
import net.starlegacy.util.blockKeyY
import net.starlegacy.util.blockKeyZ
import org.bukkit.entity.Player
import org.litote.kmongo.eq

@CommandAlias("listships")
@Suppress("unused")
object Listships: BaseCommand() {
	@Description("Lists all starships you own.")
	@Subcommand("mine")
	fun mine(sender: Player) {
		val component = text()
		component.append(text("Your Starships:").decorate(TextDecoration.BOLD))

		MongoManager.getCollection(PlayerStarshipData::class).find(PlayerStarshipData::captain eq sender.slPlayerId).forEach {
			component.append(text("\n"))
			if (it.isLockActive()) component.append(text("Locked ", NamedTextColor.RED))
			component.append(text(it.type.displayName, NamedTextColor.AQUA))
			component.append(text(" on ", NamedTextColor.GRAY))
			component.append(text(it.world, NamedTextColor.GOLD))
			component.append(text(" at ", NamedTextColor.GRAY))
			component.append(text("${blockKeyX(it.blockKey)}, ${blockKeyY(it.blockKey)}, ${blockKeyZ(it.blockKey)} ", NamedTextColor.LIGHT_PURPLE))
			component.append(text("last used ", NamedTextColor.GRAY))
			component.append(text(niceTimeFormatting(System.currentTimeMillis() - it.lastUsed),
				NamedTextColor.YELLOW))
			component.append(text(" named ", NamedTextColor.GRAY))
			component.append(text(it.name ?: "[nothing]", NamedTextColor.GREEN))
		}

		sender.sendMessage(component)
	}

	@Description("Lists all starships that are active.")
	@Subcommand("mine")
	fun active(sender: Player) {
		val component = text()
		component.append(text("Active Starships:").decorate(TextDecoration.BOLD))

		var blockCountAccumulator = 0

		ActiveStarships.all().forEach {
			blockCountAccumulator += it.blockCount

			component.append(text("\n${it.type.displayName}", NamedTextColor.AQUA))
			component.append(text(" on ", NamedTextColor.GRAY))
			component.append(text(it.world.name, NamedTextColor.GOLD))
			component.append(text(" at ", NamedTextColor.GRAY))
			component.append(text("${it.centerOfMass.x}, ${it.centerOfMass.y}, ${it.centerOfMass.z} ", NamedTextColor.LIGHT_PURPLE))
			component.append(text("piloted by ", NamedTextColor.GRAY))
			component.append(text((it as? ActivePlayerStarship)?.pilot?.name ?: "Unknown", NamedTextColor.YELLOW))
			component.append(text(" named ", NamedTextColor.GRAY))
			component.append(text((it as? ActivePlayerStarship)?.pilot?.name ?: "Unknown", NamedTextColor.GREEN))
		}

		component.append(text("\nTotal blocks: ", NamedTextColor.GRAY))
		component.append(text(blockCountAccumulator.toString(), NamedTextColor.GREEN))

		sender.sendMessage(component)
	}
}