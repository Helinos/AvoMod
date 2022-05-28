package net.helinos.avomod

import org.bukkit.Material
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockRedstoneEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.EventHandler as EventListener

/**
 * @author bush
 * @since 5/27/2022
 */
object EventHandler : Listener {

    @EventListener
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock?.type == Material.NOTE_BLOCK) {
            event.isCancelled = true
        }
    }

    @EventListener
    fun onBlockPowered(event: BlockRedstoneEvent) {
        if (event.block.type == Material.NOTE_BLOCK) event.newCurrent = 0
    }
}
