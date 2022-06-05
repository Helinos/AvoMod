package net.avocraft.avomod

import net.avocraft.avomod.AvoMod.Companion.NAME
import net.avocraft.avomod.block.BlockHandler
import net.avocraft.avomod.block.MaterialRegistry
import net.avocraft.avomod.recipe.RecipeHandler
import net.avocraft.avomod.recipe.RecipeRegistry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.bukkit.Bukkit
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

val Logger: Logger = LogManager.getLogger(NAME)

lateinit var Plugin: AvoMod

class AvoMod : JavaPlugin() {

    companion object {
        const val NAME = "AvoMod"
        const val VERSION = "1.0.0"
        const val ALIAS = "$NAME $VERSION"
    }

    init {
        Plugin = this
    }

    override fun onEnable() {
        MaterialRegistry // Need to reference this to init it
        server.pluginManager.registerEvents(BlockHandler, this)
        server.pluginManager.registerEvents(RecipeHandler, this)
        getCommand("avgive")?.run {
            setExecutor { _, _, _, args ->
                var typeName = args[1]
                if (!typeName.startsWith("avocraft:"))
                    typeName = "avocraft:$typeName"
                Bukkit.getPlayer(args[0])?.inventory?.addItem(
                    MaterialRegistry.itemForTypeName(typeName, args.getOrNull(2)?.toIntOrNull() ?: 1)
                )
                true
            }
            setTabCompleter { sender, _, _, args ->
                when (args.size) {
                    1 -> sender.server.onlinePlayers.map { it.name }.toMutableList().also {
                        it -= sender.name
                        it.add(0, sender.name)
                    }.filter {
                        it.contains(args[0])
                    }
                    2 -> MaterialRegistry.itemByTypeName.keys.toList().filter {
                        it.contains(args[1])
                    }
                    else -> if (args.size == 3 && args.getOrNull(2) == null)
                        listOf("[<count>]")
                    else emptyList<String>()
                }
            }
        }
        RecipeRegistry.registerRecipes()
        Logger.info("$ALIAS enabled")
    }

    override fun onDisable() {
        // todo: check if listeners/recipes relating to a
        //  plugin are automatically removed when it is disabled
        HandlerList.unregisterAll(this)
        // Not sure when this would be needed, but it's here
        RecipeRegistry.unregisterRecipes()
        Logger.info("$ALIAS disabled")
    }
}
