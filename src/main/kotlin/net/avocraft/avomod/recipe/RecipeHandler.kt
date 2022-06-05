package net.avocraft.avomod.recipe

import io.papermc.paper.event.player.PlayerStonecutterRecipeSelectEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.PrepareItemCraftEvent
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.ShapelessRecipe

object RecipeHandler : Listener {

    @EventHandler
    fun onCraft(event: PrepareItemCraftEvent) {
        val key = when (val recipe = event.recipe) {
            is ShapedRecipe -> recipe.key.namespace
            is ShapelessRecipe -> recipe.key.namespace
            else -> return
        }

        if (key.startsWith("minecraft")) {
            for (item in event.inventory.matrix.filterNotNull()) {
                if (item.itemMeta?.hasCustomModelData() == true) {
                    event.inventory.result = null
                }
            }
        }
    }

    @EventHandler
    fun onStoneCut(event: PlayerStonecutterRecipeSelectEvent) {
        // TODO: I'm gonna be honest I don't think a single person on earth will try to do this
    }

}