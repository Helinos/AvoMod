package net.avocraft.avomod

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockDropItemEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack

object BlockHandler : Listener {

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        val heldItem = event.itemInHand
        if (heldItem.type == Material.STONE && heldItem.itemMeta?.customModelData == 1) {
            event.blockPlaced.blockData = Bukkit.createBlockData("minecraft:note_block[instrument=banjo,note=0]")
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockDropItemEvent) {
        val blockData = event.blockState.blockData
        if (blockData.toString().contains("banjo,note=0") && event.items.size >= 1) {
            val testBlock = ItemStack(Material.STONE)
            val itemMeta = testBlock.itemMeta
            itemMeta?.setCustomModelData(1)
            itemMeta?.setDisplayName("§rTest Block")
            testBlock.itemMeta = itemMeta

            event.items[0].itemStack = testBlock
        }
    }
}