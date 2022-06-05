package net.avocraft.avomod.block

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * @author bush
 * @since 5/29/2022
 */
class AvoBlock(
    val typeName: String,
    private val displayName: String,
    private val material: Material,
    val modelId: Int,
    val instrument: String,
    val note: Int
) {
    fun item(amount: Int = 1) = ItemStack(material, amount).apply {
        val iM = this.itemMeta!!
        iM.setCustomModelData(modelId)
        iM.setDisplayName("§r$displayName")
        this.itemMeta = iM
    }

    fun asAvoItem(): AvoItem {
        return AvoItem(typeName, displayName, material, modelId)
    }
}

