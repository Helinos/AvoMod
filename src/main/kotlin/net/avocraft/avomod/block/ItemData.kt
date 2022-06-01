package net.avocraft.avomod.block

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemData(val typeName: String, displayName: String, material: Material, modelId: Int) {
    private val item = ItemStack(material, -1).apply {
        val iM = this.itemMeta!!
        iM.setCustomModelData(modelId)
        iM.setDisplayName("§r$displayName")
        this.itemMeta = iM
    }

    fun getItem(amount: Int) = item.clone().also { it.amount = amount }
}
