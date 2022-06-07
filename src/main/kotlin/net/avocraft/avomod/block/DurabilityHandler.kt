package net.avocraft.avomod.block

import net.avocraft.avomod.Logger
import net.avocraft.avomod.PLUGIN
import net.avocraft.avomod.typeNameOrDefault
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemDamageEvent
import org.bukkit.inventory.meta.Damageable
import org.bukkit.persistence.PersistentDataType

val NAMESPACED_KEY = NamespacedKey(PLUGIN, "durability")

object DurabilityHandler : Listener {

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    fun onItemDamaged(event: PlayerItemDamageEvent) {
        val item = event.item
        val typeName = item.typeNameOrDefault()
        val avoItem = MaterialRegistry.avoItemByTypeName[typeName]
        if (avoItem == null || !avoItem.isDamageable)
            return

        val iM = item.itemMeta ?: return
        val persistentDataContainer = iM.persistentDataContainer
        if (persistentDataContainer.has(NAMESPACED_KEY, PersistentDataType.INTEGER)) {
            val realDurabilityLeft = persistentDataContainer
                .get(NAMESPACED_KEY, PersistentDataType.INTEGER)!! - event.damage
            if (realDurabilityLeft > 0) {
                val realMaxDurability = avoItem.maxDurability!!.toDouble()
                persistentDataContainer.set(NAMESPACED_KEY, PersistentDataType.INTEGER, realDurabilityLeft)
                (iM as Damageable).damage =
                    (item.type.maxDurability - realDurabilityLeft / realMaxDurability * item.type.maxDurability).toInt()
                item.itemMeta = iM
            } else {
                item.amount = 0
            }
        }
    }

}