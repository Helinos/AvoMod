package net.avocraft.avomod

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import net.avocraft.avomod.block.BlockHandler
import net.avocraft.avomod.block.MaterialRegistry
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.ShapelessRecipe
import org.bukkit.plugin.java.JavaPlugin
import java.util.stream.Collectors

class AvoMod : JavaPlugin() {

    companion object {
        const val NAME = "AvoMod"
        const val VERSION = "1.0.0"
        const val ALIAS = "$NAME $VERSION"
    }

    override fun onEnable() {
        MaterialRegistry // Need to reference this to init it
        server.pluginManager.registerEvents(BlockHandler, this)

        getCommand("avgive")?.setExecutor { sender, _, _, args ->
            if (sender is Player) {
                var count = 1
                args[2].toIntOrNull()?.let {
                    count = it
                }
                val item = MaterialRegistry.itemForTypeName(args[1], count)
                Bukkit.getPlayer(args[0])?.inventory?.addItem(item)
            }
            true
        }

        getCommand("avgive")?.setTabCompleter { sender, _, _, args ->
            when {
                args.size == 1 -> {
                    val list = sender.server.onlinePlayers.stream().map(Player::getName).collect(Collectors.toList())
                    list.remove(sender.name)
                    list.add(0, sender.name)
                    list
                }
                args.size == 2 -> {
                    MaterialRegistry.itemByTypeName.keys.toList()
                }
                args.size == 3 && args[2].toIntOrNull() == null -> {
                    val list = ArrayList<String>()
                    list.add("[<count>]")
                    list
                }
                else -> {
                    val list = ArrayList<String>()
                    list
                }
            }

        }

        Logger.info("$ALIAS enabled")

        // TODO: Put recipes somewhere else
        val acceleriteShard = MaterialRegistry.ACCELERITE_SHARD.getItem(1)
        val acceleriteOre = MaterialRegistry.ACCELERITE_ORE.getItem(1)
        furnaceRecipe(this, acceleriteOre, acceleriteShard, 2.0f, 200)

        val acceleriteIngot = MaterialRegistry.ACCELERITE_INGOT.getItem(1)
        val key = NamespacedKey(this, "test")
        val hey = ShapelessRecipe(key, acceleriteIngot)
        hey.addIngredient(ExactChoice(acceleriteShard))
        hey.addIngredient(ExactChoice(acceleriteShard))
        hey.addIngredient(ExactChoice(acceleriteShard))
        hey.addIngredient(ExactChoice(acceleriteShard))
        hey.addIngredient(Material.IRON_INGOT)
        Bukkit.addRecipe(hey)
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
        inputName = it.displayName.lowercase().replace(' ', '_')
    }

    result.itemMeta?.let {
        resultName = it.displayName.lowercase().replace(' ', '_')
    }

    val key = NamespacedKey(plugin, "${inputName}_to_${resultName}_furnace")
    val inputChoice = ExactChoice(input)
    val recipe = FurnaceRecipe(key, result, inputChoice, experience, cookingTime)
    Bukkit.addRecipe(recipe)
}
