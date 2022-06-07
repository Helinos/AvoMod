package net.avocraft.avomod.block

import com.google.common.collect.Multimap
import net.avocraft.avomod.Logger
import net.avocraft.avomod.PLUGIN
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.Damageable
import org.bukkit.inventory.meta.LeatherArmorMeta
import org.bukkit.persistence.PersistentDataType

class AvoItem(
    val typeName: String,
    private val displayName: String,
    private val material: Material,
    private val modelId: Int,
    private val color: Color? = null,
    private val attributeModifiers: Multimap<Attribute, AttributeModifier>? = null,
    val maxDurability: Int? = null
) {
    fun item(amount: Int = 1) = ItemStack(material, amount).apply {
        var iM = this.itemMeta!!
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
            iM = lAM
        }

        // Durability
        if (maxDurability != null) {
            iM.persistentDataContainer.set(
                NamespacedKey(PLUGIN, "durability"),
                PersistentDataType.INTEGER,
                maxDurability
            )
        }

        this.itemMeta = iM
    }

//    fun isDamageable(): Boolean {
//        val iM = this.item().itemMeta
//        return (iM is Damageable && maxDurability != null)
//    }

    val isDamageable = maxDurability != null
}