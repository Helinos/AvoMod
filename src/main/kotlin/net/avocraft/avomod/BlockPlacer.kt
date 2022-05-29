package net.avocraft.avomod

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

object BlockPlacer : Listener {

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val itemInHand = event.itemInHand
        if (itemInHand.type == Material.STONE && itemInHand.itemMeta?.customModelData == 1) {
            event.blockPlaced.blockData = Bukkit.createBlockData("minecraft:note_block[instrument=banjo,note=0]")
        }
    }
}