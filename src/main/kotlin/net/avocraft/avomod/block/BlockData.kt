package net.avocraft.avomod.block

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author bush
 * @since 5/29/2022
 */
class BlockData(
    val typeName: String,
    displayName: String,
    material: Material,
    val modelId: Int,
    val instrument: String,
    val note: Int
) {
    val item = ItemStack(material, 1).apply {
        val iM = this.itemMeta
        iM?.setCustomModelData(modelId)
        iM?.setDisplayName("§r$displayName")
        this.itemMeta = iM
    }
}

