package net.avocraft.avomod.block

import com.google.common.collect.HashBiMap
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.inventory.ItemStack

/**
 * @author bush
 * @since 5/29/2022
 */
object BlockRegistry {
    private val reservedNoteBlocks = HashSet<NoteBlock>()

    val blockByTypeName: HashBiMap<String, BlockData> = HashBiMap.create()
    private val blockByNbId = HashBiMap.create<String, BlockData>()
    private val blockByModelId = HashBiMap.create<Int, BlockData>()

    init {
        register(
            BlockData("test_block","Test Block", Material.STONE, 1, "banjo", 0),
            BlockData("end_shard_ore","End Shard Ore", Material.END_STONE, 2, "banjo", 1)
        )
    }

    private fun register(vararg blocks: BlockData) {
        require(blocks.size <= 384) { "Too many blocks!" }

        for (block in blocks) {
            // Add to lists
            blockByTypeName[block.typeName] = block
            blockByNbId[block.instrument + block.note] = block
            blockByModelId[block.modelId] = block

            // Reserve Noteblocks
            val data = noteBlockForModelId(block.modelId)
            reservedNoteBlocks.add(data)

        }
    }

    fun isReservedNoteblock(block: Block) = block.blockData in reservedNoteBlocks

    fun itemForTypeName(typeName: String): ItemStack? {
        return blockByTypeName[typeName]?.item
    }

    fun itemForNoteBlock(block: BlockState): ItemStack? {
        val (_, _, instrument, _, note) = block.blockData.asString.split('[','=',',',']') // TODO: heheheha this is very scuffed
        val nbId = instrument + note
        return blockByNbId[nbId]?.item
    }

    fun noteBlockForModelId(modelId: Int): NoteBlock {
        val block = blockByModelId[modelId]
        val instrument = block?.instrument
        val note = block?.note

        return Bukkit.createBlockData(
            Material.NOTE_BLOCK,
            "[instrument=$instrument,note=$note]"
        ) as NoteBlock
    }
}
