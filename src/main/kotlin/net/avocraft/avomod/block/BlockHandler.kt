package net.avocraft.avomod.block

import net.avocraft.avomod.Logger
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
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
            if (MaterialRegistry.isReservedNoteblock(event.clickedBlock!!)) {
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
        if (MaterialRegistry.isReservedNoteblock(event.block)) event.isCancelled = true
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        // ItemStack#hashCode is overridden, this will work
        // This throws a fat ass error whenever you place an item that isn't one of ours
        event.itemInHand.itemMeta?.customModelData?.let { // If the item has CustomModelData (Same as modelId)
            val block = MaterialRegistry.noteBlockForModelId(it)
            event.block.blockData = block
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

        Logger.info(event.blockState.toString())
        MaterialRegistry.itemForNoteBlock(event.blockState)?.let {
            Logger.info("hey")
            event.items[0]?.itemStack = it
        }
    }

    // Cancel instrument changes when the block underneath is updated
    @EventHandler
    fun onBlockPhysics(event: BlockPhysicsEvent) {
        // todo update this to check if the block change will turn a normal noteblock into a reserved block
        val aboveBlock = event.block.getRelative(0, 1, 0)
        if (aboveBlock.type == Material.NOTE_BLOCK) {
            recursiveVerticalCheck(aboveBlock)
            event.isCancelled = true
        }
        if (event.block.type == Material.NOTE_BLOCK) {
            event.isCancelled = true
        }
        event.block.state.update(true, false)
    }

    private tailrec fun recursiveVerticalCheck(block: Block) {
        if (block.type == Material.NOTE_BLOCK) {
            block.state.update(true, true)
        }
        val nextBlock = block.getRelative(0, 1, 0)
        if (nextBlock.type == Material.NOTE_BLOCK) {
            recursiveVerticalCheck(nextBlock)
        }
    }
}
