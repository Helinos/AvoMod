package net.avocraft.avomod

import net.avocraft.avomod.block.MaterialRegistry
import org.bukkit.Bukkit
import org.bukkit.Material.*
import org.bukkit.NamespacedKey
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.ShapelessRecipe

object RecipeRegistry {
    private val keys = mutableMapOf<String, NamespacedKey>()

    // todo: maybe we could make recipies part of the item/blockdata class? might do that eventually
    fun registerRecipes() {
        shardFurnaceRecipe()
        ingotCraftingRecipe()
        berryPieCraftingRecipe()
    }

    fun unregisterRecipes() {
        keys.values.forEach(Bukkit::removeRecipe)
    }

    private fun getKey(result: ItemStack, type: String) = keys.getOrPut(result.toString() + type) {
        NamespacedKey(Plugin, "${result.metaNameOrDefault()}_${type}_${Bukkit.getRecipesFor(result).size}")
    }

    private fun shardFurnaceRecipe() {
        val shard = MaterialRegistry.ACCELERITE_SHARD.item()
        Bukkit.addRecipe(
            FurnaceRecipe(
                getKey(shard, "furnace"),
                shard,
                ExactChoice(MaterialRegistry.ACCELERITE_ORE.item()),
                2f,
                200
            )
        )
    }

    private fun ingotCraftingRecipe() {
        val shard = MaterialRegistry.ACCELERITE_SHARD.item()
        val ingot = MaterialRegistry.ACCELERITE_INGOT.item()
        Bukkit.addRecipe(ShapelessRecipe(getKey(ingot, "crafting"), ingot).apply {
            addIngredient(ExactChoice(shard))
            addIngredient(ExactChoice(shard))
            addIngredient(ExactChoice(shard))
            addIngredient(ExactChoice(shard))
            addIngredient(IRON_INGOT)
        })
    }

    private fun berryPieCraftingRecipe() {
        val berryPie = MaterialRegistry.BERRY_PIE.item()
        Bukkit.addRecipe(ShapelessRecipe(getKey(berryPie, "crafting"), berryPie).apply {
            addIngredient(WHEAT)
            addIngredient(SWEET_BERRIES)
            addIngredient(SUGAR)
            addIngredient(EGG)
        })
    }
}
