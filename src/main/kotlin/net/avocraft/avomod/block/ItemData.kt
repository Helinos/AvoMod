package net.avocraft.avomod.block

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemData(
    val typeName: String,
    private val displayName: String,
    private val material: Material,
    private val modelId: Int
) {
    fun getItem(amount: Int) = ItemStack(material, amount).apply {
        val iM = this.itemMeta!!
        iM.setCustomModelData(modelId)
        iM.setDisplayName("§r$displayName")
        this.itemMeta = iM
    }
}