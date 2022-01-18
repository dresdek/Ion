package net.starlegacy.feature.multiblock.autocrafter

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import net.minecraft.server.v1_16_R3.InventoryCrafting
import net.minecraft.server.v1_16_R3.MinecraftServer
import net.minecraft.server.v1_16_R3.NonNullList
import net.minecraft.server.v1_16_R3.Recipes
import net.starlegacy.feature.machine.PowerMachines
import net.starlegacy.feature.multiblock.FurnaceMultiblock
import net.starlegacy.feature.multiblock.MultiblockShape
import net.starlegacy.feature.multiblock.PowerStoringMultiblock
import net.starlegacy.util.CBItemStack
import net.starlegacy.util.NMSItemStack
import net.starlegacy.util.add
import net.starlegacy.util.getFacing
import net.starlegacy.util.getStateIfLoaded
import net.starlegacy.util.nms
import net.starlegacy.util.orNull
import net.starlegacy.util.rightFace
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Furnace
import org.bukkit.block.Sign
import org.bukkit.event.inventory.FurnaceBurnEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import java.util.HashMap
import java.util.Optional

private const val POWER_USAGE_PER_INGREDIENT = 50

abstract class AutoCrafterMultiblock(
        tierText: String,
        private val tierMaterial: Material,
        private val iterations: Int
) : PowerStoringMultiblock(), FurnaceMultiblock {
    override val name = "autocrafter"

    override fun MultiblockShape.buildStructure() {
        z(+0) {
            y(-1) {
                x(-2).type(tierMaterial)
                x(-1).anyGlassPane()
                x(+0).wireInputComputer()
                x(+1).anyGlassPane()
                x(+2).type(tierMaterial)
            }

            y(+0) {
                x(-2).type(tierMaterial)
                x(-1).anyGlassPane()
                x(+0).machineFurnace()
                x(+1).anyGlassPane()
                x(+2).type(tierMaterial)
            }
        }

        z(+1) {
            y(-1) {
                x(-2).anyGlass() // input pipe
                x(-1).titaniumBlock()
                x(+0).craftingTable()
                x(+1).titaniumBlock()
                x(+2).extractor()
            }

            y(+0) {
                x(-2).anyPipedInventory()
                x(-1).endRod()
                x(+0).anyType(Material.DISPENSER, Material.DROPPER)
                x(+1).endRod()
                x(+2).anyPipedInventory()
            }
        }

        z(+2) {
            y(-1) {
                x(-2).type(tierMaterial)
                x(-1).anyGlassPane()
                x(+0).anyGlass()
                x(+1).anyGlassPane()
                x(+2).type(tierMaterial)
            }

            y(+0) {
                x(-2).type(tierMaterial)
                x(-1).anyGlassPane()
                x(+0).anyGlass()
                x(+1).anyGlassPane()
                x(+2).type(tierMaterial)
            }
        }
    }

    override val signText: List<String> = createSignText(
            line1 = "&aAuto",
            line2 = "&6Crafter",
            line3 = null,
            line4 = tierText
    )

    private fun getInput(sign: Sign): InventoryHolder? {
        val forward = sign.getFacing().oppositeFace
        val left = forward.rightFace.oppositeFace
        val x = sign.x + forward.modX * 2 + left.modX * 2
        val y = sign.y + forward.modY * 2 + left.modY * 2
        val z = sign.z + forward.modZ * 2 + left.modZ * 2
        return getStateIfLoaded(sign.world, x, y, z) as? InventoryHolder
    }

    private fun getRecipeHolder(sign: Sign): InventoryHolder? {
        val forward = sign.getFacing().oppositeFace
        val x = sign.x + forward.modX * 2
        val y = sign.y + forward.modY * 2
        val z = sign.z + forward.modZ * 2
        return getStateIfLoaded(sign.world, x, y, z) as? InventoryHolder
    }

    private fun getOutput(sign: Sign): InventoryHolder? {
        val forward = sign.getFacing().oppositeFace
        val right = forward.rightFace
        val x = sign.x + forward.modX * 2 + right.modX * 2
        val y = sign.y + forward.modY * 2 + right.modY * 2
        val z = sign.z + forward.modZ * 2 + right.modZ * 2
        return getStateIfLoaded(sign.world, x, y, z) as? InventoryHolder
    }

    override fun onFurnaceTick(event: FurnaceBurnEvent, furnace: Furnace, sign: Sign) {
        event.isCancelled = false
        event.isBurning = false
        event.burnTime = 20

        val input: InventoryHolder = getInput(sign) ?: return
        val recipeHolder: InventoryHolder = getRecipeHolder(sign) ?: return
        val output: InventoryHolder = getOutput(sign) ?: return

        // material data of each item in the recipe holder, used as the crafting grid
        val grid: List<Material?> = recipeHolder.inventory.map { it?.type }

        var power = PowerMachines.getPower(sign, fast = true)

        // result item of this recipe
        val result: ItemStack = recipeCache[grid].orElse(null)?.clone() ?: run {
            return
        }

        val inputInventory = input.inventory

        val powerUsage = grid.distinct().count() * POWER_USAGE_PER_INGREDIENT

        try {
            for (iteration in (1..iterations)) {
                if (power < powerUsage) {
                    return
                }

                val removeSlots = mutableListOf<Int>() // can be multiple times per slot, so list, not set
                var requiredIngredients = 0
                var matchedIngredients = 0

                // for each slot in the crafting grid,
                // if it's not null,
                // increment required ingredients to keep track of how many are needed,
                // and loop through the input inventory,
                // if an item's data matches the ingredient,
                // flag that slot for removal,a
                // increment matched ingredients,
                // and move on to the next ingredient
                ingredientLoop@
                for (ingredient: Material? in grid) {
                    if (ingredient != null) {
                        requiredIngredients++
                        for ((index: Int, item: ItemStack?) in inputInventory.withIndex()) {
                            // if it matches AND we haven't already taken too much from it, use it
                            if (item?.type == ingredient && item.amount >= removeSlots.count { it == index } + 1) {
                                removeSlots += index
                                matchedIngredients++
                                continue@ingredientLoop
                            }
                        }
                    }
                }

                // stop iterating if not all of the ingredients were found
                if (matchedIngredients != requiredIngredients) {
                    return
                }

                val remaining: HashMap<Int, ItemStack> = output.inventory.addItem(result)

                if (remaining.isNotEmpty()) {
                    val added = result.amount - remaining.values.sumBy { it.amount }
                    check(added >= 0)
                    if (added > 0) {
                        output.inventory.removeItem(result.clone().apply { amount = added })
                    }
                    return
                }

                power -= powerUsage

                // below code leaves incomplete stacks, but might be faster
                /*// attempt to add the item to the output inventory (we already checked that we can remove it from the input)
                var fit = false
                for ((index: Int, item: ItemStack?) in output.inventory.contents.withIndex()) {
                    if (item == null) {
                        output.inventory.setItem(index, result)
                        fit = true
                        break
                    } else if(item.isSimilar(result) && item.amount + result.amount <= item.maxStackSize) {
                        item.amount += result.amount
                        fit = true
                        break
                    }
                }

                if (!fit) {
                    return
                }*/

                // remove the items
                for (index in removeSlots) {
                    // since AFAIK recipes only call for one item per ingredient, just decrement the amount
                    // it will automatically remove the item if the amount hits 0
                    input.inventory.getItem(index)!!.amount--
                }
            }
        } finally {
            PowerMachines.setPower(sign, power)
        }
    }
}

private val itemsField = InventoryCrafting::class.java.getDeclaredField("items").apply { isAccessible = true }
private fun getItems(inventoryCrafting: InventoryCrafting): NonNullList<NMSItemStack> {
    @Suppress("UNCHECKED_CAST")
    return itemsField[inventoryCrafting] as NonNullList<NMSItemStack>
}

private val recipeCache: LoadingCache<List<Material?>, Optional<ItemStack>> = CacheBuilder.newBuilder().build(CacheLoader.from { items ->
    requireNotNull(items)
    val inventoryCrafting = InventoryCrafting(/*container=*/null, /*width=*/3, /*height=*/3)

    val inventoryItems: NonNullList<NMSItemStack> = getItems(inventoryCrafting)
    for ((index: Int, material: Material?) in items.withIndex()) {
        val item: NMSItemStack = if (material != null) CBItemStack.asNMSCopy(ItemStack(material, 1))
        else NMSItemStack.NULL_ITEM
        inventoryItems[index] = item
    }

    val result: NMSItemStack? = MinecraftServer.getServer().craftingManager
            .craft(Recipes.CRAFTING, inventoryCrafting, Bukkit.getWorlds().first().nms)
            .orNull()?.a(inventoryCrafting)

    return@from Optional.ofNullable(result?.asBukkitCopy())
})
