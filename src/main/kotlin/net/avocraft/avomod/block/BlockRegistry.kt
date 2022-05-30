package net.avocraft.avomod.block

import com.google.common.collect.HashBiMap
import org.bukkit.Bukkit
import org.bukkit.Instrument
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.inventory.ItemStack

/**
 * @author bush
 * @since 5/29/2022
 */
object BlockRegistry {
    private val reservedNoteblocks = HashBiMap.create<NoteBlock, ItemStack>()

    init {
        register(
            BlockData("Test Block", Material.STONE, 1),
            BlockData("Evil Block", Material.EVOKER_SPAWN_EGG, 1)
        )
    }

    private fun register(vararg blocks: BlockData) {
        require(blocks.size <= 384) { "Too many blocks!" }
        var index = 0
        // Reversed to try to use uncommon instruments first
        for (instrument in Instrument.values().reversed()) {
            for (note in 0..24) {
                val data = Bukkit.createBlockData(
                    Material.NOTE_BLOCK,
                    "instrument=${instrument.name.lowercase()},note=$note"
                ) as NoteBlock
                reservedNoteblocks[data] = blocks[index].item
                if (++index > blocks.size) return
            }
        }
    }

    fun isReservedNoteblock(block: Block) = block.blockData in reservedNoteblocks

    fun itemForNoteblock(block: Block) = reservedNoteblocks[block.blockData]?.clone()

    fun noteblockForItem(itemStack: ItemStack) = reservedNoteblocks.inverse()[itemStack]
}
