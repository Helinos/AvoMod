package net.avocraft.avomod.block

import org.bukkit.ChatColor.RESET
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class AvoItem(
    val typeName: String,
    private val displayName: String,
    private val material: Material,
    private val modelId: Int
) {
    fun item(amount: Int = 1) = ItemStack(material, amount).apply {
        val iM = this.itemMeta
        iM?.setCustomModelData(modelId)
        iM?.setDisplayName("§r$displayName")
        this.itemMeta = iM
    }
}
