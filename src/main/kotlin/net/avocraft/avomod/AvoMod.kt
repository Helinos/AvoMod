package net.avocraft.avomod

import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import net.avocraft.avomod.block.BlockHandler
import net.avocraft.avomod.block.BlockRegistry
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.inventory.FurnaceRecipe
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class AvoMod : JavaPlugin() {

    companion object {
        const val NAME = "AvoMod"
        const val VERSION = "1.0.0"
        const val ALIAS = "$NAME $VERSION"
    }

    override fun onEnable() {
        BlockRegistry // Need to reference this to init it
        server.pluginManager.registerEvents(BlockHandler, this)

        getCommand("avgive")?.setExecutor { sender, _, _, args ->
            if (sender is Player) {
                val item = BlockRegistry.itemForTypeName(args[0])
                sender.player?.inventory?.addItem(item)
            }
            true
        }

        getCommand("avgive")?.setTabCompleter { _, _, _, _ ->
            BlockRegistry.blockByTypeName.keys.toList()
        }

        Logger.info("$ALIAS enabled")

        // TODO: Test item and recipe
        val coalCoke = ItemStack(Material.PAPER, 1)
        val itemMeta = coalCoke.itemMeta
        itemMeta?.setCustomModelData(1)
        itemMeta?.setDisplayName("§rCoal Coke")
        coalCoke.itemMeta = itemMeta
        val key = NamespacedKey(this, "coal_coke")
        val hey = FurnaceRecipe(key, coalCoke, Material.COAL, 0.1f, 400)
        Bukkit.addRecipe(hey)
    }

    override fun onDisable() {
        // todo: idk if paper does this already
        HandlerList.unregisterAll(this)
        Logger.info("$ALIAS disabled")
    }
}
