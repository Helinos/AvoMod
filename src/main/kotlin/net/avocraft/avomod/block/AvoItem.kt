package net.avocraft.avomod.block

import com.google.common.collect.Multimap
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemStack

class AvoItem(
    val typeName: String,
    private val displayName: String,
    private val material: Material,
    private val modelId: Int,
    // private var hasAttributeModifiers: Boolean,
    private var attributeModifiers: Multimap<Attribute, AttributeModifier>? = null,
) {
    fun item(amount: Int = 1) = ItemStack(material, amount).apply {
        val iM = this.itemMeta!!
        iM.setCustomModelData(modelId)
        iM.setDisplayName("§r$displayName")
        if (attributeModifiers != null) {
            iM.attributeModifiers = attributeModifiers
        }
        this.itemMeta = iM
    }
}