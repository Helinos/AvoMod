package net.avocraft.avomod.block

import org.bukkit.ChatColor.RESET
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemData(
    val typeName: String,
    private val displayName: String,
    private val material: Material,
    private val modelId: Int
) {
    val item
        get() = ItemStack(material, 1).apply {
            itemMeta!!.setCustomModelData(modelId)
            itemMeta!!.setDisplayName("$RESET$displayName")
        }
}
