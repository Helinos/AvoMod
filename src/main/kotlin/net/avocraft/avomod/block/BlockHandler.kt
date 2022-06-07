package net.avocraft.avomod.block

import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.BlockFace.UP
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.*
import org.bukkit.event.player.PlayerInteractEvent

/**
 * @author bush
 * @since 5/27/2022
 */
object BlockHandler : Listener {

    // Cancel blockstate changes when right-clicked
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        // If the player is holding something use it as normal
        // This makes it so blocks only update on the server and won't play sounds
        // This could also lead to unexpected behavior if the item being used is a fishing rod or smth
        if (event.action == Action.RIGHT_CLICK_BLOCK && event.clickedBlock?.type == Material.NOTE_BLOCK) {
            if (MaterialRegistry.isReservedNoteBlock(event.clickedBlock!!)) {
                if (event.hasItem()) event.player.isSneaking = true
                else event.isCancelled = true
            } else {
                // todo stop people from tuning to a reserved block
                // if next tune is reserved, skip past it
                // ill do this later
            }
        }
    }

    @EventHandler
    fun onNotePlay(event: NotePlayEvent) {
        if (MaterialRegistry.isReservedNoteBlock(event.block)) event.isCancelled = true
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        // ItemStack#hashCode is overridden, this will work
        if (event.itemInHand.itemMeta?.hasCustomModelData() == true) {
            event.itemInHand.itemMeta?.customModelData?.let { // If the item has CustomModelData (Same as modelId)
                val block = MaterialRegistry.noteBlockForModelId(it)
                event.block.blockData = block
            }
        }
    }

    @EventHandler
    fun onBlockBreak(event: BlockDropItemEvent) {
        if (
            event.player.gameMode == GameMode.CREATIVE ||
            event.blockState.type != Material.NOTE_BLOCK
        ) {
            return
        }

        MaterialRegistry.itemForNoteBlock(event.blockState)!!.let {
            event.items[0]?.itemStack = it
        }
    }

    // Cancel instrument changes when the block underneath is updated
    @EventHandler
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        // todo update this to check if the block change will turn a normal noteblock into a reserved block
        var aboveBlock = event.block.getRelative(UP)
        while (aboveBlock.type == Material.NOTE_BLOCK) {
            event.isCancelled = true
            aboveBlock.state.update(true, true)
            aboveBlock = aboveBlock.getRelative(UP)
        }
        if (event.block.type == Material.NOTE_BLOCK) {
            event.isCancelled = true
            event.block.state.update(true, false)
        }
    }

    @EventHandler
    fun onBlockDamage(event: BlockDamageEvent) {
        if (event.block.type == Material.NOTE_BLOCK && MaterialRegistry.itemForNoteBlock(event.block.state) != null) {
            // There is probably a better way to do this
            val modelId = MaterialRegistry.itemForNoteBlock(event.block.state)!!.itemMeta!!.customModelData
            val avoBlock = MaterialRegistry.avoBlockByModelId[modelId]!!
            val hardness = avoBlock.hardness
            TODO()
        }
    }

    @EventHandler
    fun onBlockDamageAbort(event: BlockDamageAbortEvent) {
        TODO()
    }
}
