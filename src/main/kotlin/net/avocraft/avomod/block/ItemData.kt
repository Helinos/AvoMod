package net.avocraft.avomod.block

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemData(
    val typeName: String,
    val displayName: String,
    val material: Material,
    val modelId: Int
) {
    fun getItem(amount: Int) = ItemStack(material, amount).apply {
        val iM = this.itemMeta
        iM?.setCustomModelData(modelId)
        iM?.setDisplayName("§r$displayName")
        this.itemMeta = iM
    }
}