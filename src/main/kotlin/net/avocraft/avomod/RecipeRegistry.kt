package net.avocraft.avomod

import kotlinx.serialization.json.*
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.RecipeChoice.ExactChoice
import org.bukkit.inventory.ShapelessRecipe
import java.nio.file.Files
import kotlin.io.path.name
import kotlin.io.path.readText

object RecipeRegistry {
    private val keys = mutableMapOf<String, NamespacedKey>()

    // todo: error handling could be better on all of this, right now it just
    //  throws if anything is null. we could add a more helpful message or something.

    fun registerRecipes() {
        Files.walk(getResource("/recipes")).filter { it.name.endsWith(".json") }.forEach { file ->
            val json = Json.parseToJsonElement(file.readText()).jsonObject
            when (file.parent.name) {
                "furnace" -> {
                    val result = stackOf(json["result"]!!.toString())
                    Bukkit.addRecipe(
                        FurnaceRecipe(
                            getKey(result, "furnace"),
                            result,
                            ExactChoice(stackOf(json["input"]!!.toString())),
                            json["experience"]!!.jsonPrimitive.float,
                            json["time"]!!.jsonPrimitive.int
                        )
                    )
                }
                "shapeless" -> {
                    val result = stackOf(json["result"]!!.toString())
                    Bukkit.addRecipe(ShapelessRecipe(getKey(result, "crafting"), result).apply {
                        json["ingredients"]!!.jsonObject.forEach { item, count ->
                            val stack = stackOf(item)
                            repeat(count.jsonPrimitive.int) {
                                addIngredient(ExactChoice(stack))
                            }
                        }
                    })
                }
                "idk?? are we going to have shaped 3x3 and shaped 2x2 or how is this going to work" -> TODO()
            }
        }
    }

    private fun getKey(result: ItemStack, type: String) = keys.getOrPut(result.toString() + type) {
        NamespacedKey(Plugin, "${result.metaNameOrDefault()}_${type}_${Bukkit.getRecipesFor(result).size}")
    }

    fun unregisterRecipes() {
        keys.values.forEach(Bukkit::removeRecipe)
    }
}
