package net.helinos.avomod

import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockPhysicsEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.EventHandler as EventListener
import org.bukkit.event.block.NotePlayEvent

/**
 * @author bush
 * @since 5/27/2022
 */
object EventHandler : Listener {

    // Cancel blockstate changes when right-clicked
    @EventListener
    fun onPlayerInteract(event: PlayerInteractEvent) {
        // If the player is holding something use it as normal
        // This makes it so blocks only update on the server and won't play sounds
        // This could also lead to unexpected behavior if the item being used is a fishing rod or smth
        if (event.item?.type?.name != null) {
            event.player.isSneaking = true
            return
        }

        if (
            event.action == Action.RIGHT_CLICK_BLOCK &&
            event.clickedBlock?.type == Material.NOTE_BLOCK
        ) {
            event.isCancelled = true
        }
    }

    @EventListener
    fun onNotePlay(event: NotePlayEvent) {
        event.isCancelled = true
    }

    // Cancel instrument changes when the block underneath is updated
    @EventListener
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        val aboveBlock = event.block.location.add(0.0, 1.0, 0.0).block
        if (aboveBlock.type == Material.NOTE_BLOCK) {
            recursiveVerticalCheck(aboveBlock)
            event.isCancelled = true
        }
        if (event.block.type == Material.NOTE_BLOCK) {
            event.isCancelled = true
        }
        event.block.state.update(true, false)
    }
}

private fun recursiveVerticalCheck(block: Block) {
    if (block.type == Material.NOTE_BLOCK) {
        block.state.update(true, true)
    }
    val nextBlock = block.location.add(0.0, 1.0, 0.0).block
    if (nextBlock.type == Material.NOTE_BLOCK) {
        recursiveVerticalCheck(nextBlock)
    }
}
