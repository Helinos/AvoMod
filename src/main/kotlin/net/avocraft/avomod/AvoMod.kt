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
                val item = MaterialRegistry.itemForTypeName(args[1], args[2].toInt())
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
