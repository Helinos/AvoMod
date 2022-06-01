package net.avocraft.avomod

import net.avocraft.avomod.AvoMod.Companion.NAME
import net.avocraft.avomod.block.BlockHandler
import net.avocraft.avomod.block.MaterialRegistry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.HandlerList
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.plugin.java.JavaPlugin

val Logger: Logger = LogManager.getLogger(NAME)

class AvoMod : JavaPlugin() {

    companion object {
        const val NAME = "AvoMod"
        const val VERSION = "1.0.0"
        const val ALIAS = "$NAME $VERSION"
    }

    override fun onEnable() {
        MaterialRegistry // Need to reference this to init it
        server.pluginManager.registerEvents(BlockHandler, this)
        getCommand("avgive")?.run {
            setExecutor { _, _, _, args ->
                Bukkit.getPlayer(args[0])?.inventory?.addItem(
                    MaterialRegistry.itemForTypeName(args[1], args[2].toIntOrNull() ?: 1)
                )
                true
            }
            setTabCompleter { sender, _, _, args ->
                when (args.size) {
                    1 -> sender.server.onlinePlayers.map { it.name }.toMutableList().also {
                        it -= sender.name
                        it.add(0, sender.name)
                    }
                    2 -> MaterialRegistry.itemByTypeName.keys.toList()
                    else -> if (args.size == 3 && args[2].toIntOrNull() == null)
                        listOf("[<count>]")
                    else emptyList<String>()
                }
            }
        }

        // TODO: Put recipes somewhere else
        // Accelerite Ore Furnace
        val acceleriteShard = MaterialRegistry.ACCELERITE_SHARD.getItem(1)
        val acceleriteOre = MaterialRegistry.ACCELERITE_ORE.getItem(1)
        furnaceRecipe(this, acceleriteOre, acceleriteShard, 2.0f, 200)

        // Accelerite Ingot
        val acceleriteIngot = MaterialRegistry.ACCELERITE_INGOT.getItem(1)
        val key1 = NamespacedKey(this, "test")
        val hey = ShapelessRecipe(key1, acceleriteIngot)
        hey.addIngredient(ExactChoice(acceleriteShard))
        hey.addIngredient(ExactChoice(acceleriteShard))
        hey.addIngredient(ExactChoice(acceleriteShard))
        hey.addIngredient(ExactChoice(acceleriteShard))
        hey.addIngredient(Material.IRON_INGOT)
        Bukkit.addRecipe(hey)

        // Berry Pie
        val berryPie = MaterialRegistry.BERRY_PIE.getItem(1)
        val key2 = craftingKeyGen(this, berryPie)
        val hey2 = ShapelessRecipe(key2, berryPie)
        hey2.addIngredient(Material.WHEAT)
        hey2.addIngredient(Material.SWEET_BERRIES)
        hey2.addIngredient(Material.SUGAR)
        hey2.addIngredient(Material.EGG)
        Bukkit.addRecipe(hey2)

        Logger.info("$ALIAS enabled")
    }

    override fun onDisable() {
        // todo: idk if paper does this already
        HandlerList.unregisterAll(this)
        Logger.info("$ALIAS disabled")
    }
}

fun furnaceRecipe(plugin: JavaPlugin, input: ItemStack, result: ItemStack, experience: Float, cookingTime: Int) {
    var inputName = input.type.name
    var resultName = result.type.name

    input.itemMeta?.let {
        inputName = it.displayName.toID()
    }

    result.itemMeta?.let {
        resultName = it.displayName.toID()
    }

    val key = NamespacedKey(plugin, "${inputName}_to_${resultName}_furnace")
    val inputChoice = ExactChoice(input)
    val recipe = FurnaceRecipe(key, result, inputChoice, experience, cookingTime)
    Bukkit.addRecipe(recipe)
}

fun craftingKeyGen(plugin: JavaPlugin, result: ItemStack): NamespacedKey {
    var resultName = result.type.name

    result.itemMeta?.let {
        resultName = it.displayName.toID()
    }

    return NamespacedKey(plugin, "${resultName}_crafting_${Bukkit.getRecipesFor(result).size}")
}
