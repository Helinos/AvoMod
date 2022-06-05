package net.avocraft.avomod.block

import com.google.common.collect.Multimap
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta

class AvoItem(
    val typeName: String,
    private val displayName: String,
    private val material: Material,
    private val modelId: Int,
    // private var hasAttributeModifiers: Boolean,
    private var color: Color? = null,
    private var attributeModifiers: Multimap<Attribute, AttributeModifier>? = null
) {
    fun item(amount: Int = 1) = ItemStack(material, amount).apply {
        val iM = this.itemMeta!!
        iM.setCustomModelData(modelId)
        iM.setDisplayName("§r$displayName")

        // Item attributes
        if (attributeModifiers != null)
            iM.attributeModifiers = attributeModifiers

        // Custom Armor Colors/Textures
        if (color != null) {
            val lAM = iM as LeatherArmorMeta
            lAM.setColor(color)
            lAM.addItemFlags(ItemFlag.HIDE_DYE)
            this.itemMeta = lAM
            return@apply
        }

        this.itemMeta = iM
    }
}