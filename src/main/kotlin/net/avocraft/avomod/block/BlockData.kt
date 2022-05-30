package net.avocraft.avomod.block

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author bush
 * @since 5/29/2022
 */
class BlockData(
    name: String,
    item: Material,
    modelData: Int
) {
    val item = ItemStack(item, 1).apply {
        itemMeta?.setCustomModelData(modelData)
        itemMeta?.setDisplayName(name)
    }
}
