package net.avocraft.avomod.block

import org.bukkit.ChatColor.RESET
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemData(val typeName: String, displayName: String, material: Material, modelId: Int) {
    private val item = ItemStack(material, -1).apply {
        itemMeta!!.setCustomModelData(modelId)
        itemMeta!!.setDisplayName("$RESET$displayName")
    }

    fun getItem(amount: Int) = item.clone().also { it.amount = amount }
}
