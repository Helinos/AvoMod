package net.avocraft.avomod

import net.avocraft.avomod.block.BlockHandler
import net.avocraft.avomod.block.BlockRegistry
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
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
        getCommand("avgive")?.setExecutor { sender, _, _, _ ->
            if (sender is Player) {
                val testBlock = ItemStack(Material.STONE)
                val itemMeta = testBlock.itemMeta
                itemMeta?.setCustomModelData(1)
                itemMeta?.setDisplayName("§rTest Block")
                testBlock.itemMeta = itemMeta

                sender.player?.inventory?.addItem(testBlock)
            }
            true
        }
        Logger.info("$ALIAS enabled")
    }

    override fun onDisable() {
        // todo: idk if paper does this already
        HandlerList.unregisterAll(this)
        Logger.info("$ALIAS disabled")
    }
}
