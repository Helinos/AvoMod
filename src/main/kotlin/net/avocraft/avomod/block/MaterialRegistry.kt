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
object MaterialRegistry {
    private val reservedNoteBlocks = HashSet<NoteBlock>()

    private val blockByNbId = HashBiMap.create<String, BlockData>()
    private val blockByModelId = HashBiMap.create<Int, BlockData>()

    val TEST_BLOCK = registerBlock("test_block","Test Block", Material.STONE, 1, "banjo", 0)
    val ACCELERITE_ORE = registerBlock("accelerite_ore","Accelerite Ore", Material.END_STONE, 2, "banjo", 1)

    val COAL_COKE = registerItem("coal_coke", "Coal Coke", Material.PAPER, 3)
    val ACCELERITE_SHARD = registerItem("accelerite_shard", "Accelerite Shard", Material.PAPER, 4)
    val ACCELERITE_INGOT = registerItem("accelerite_ingot", "Accelerite Ingot", Material.PAPER, 5)

    private fun registerBlock(
        typeName: String,
        displayName: String,
        material: Material,
        modelId: Int,
        instrument: String,
        note: Int,
    ): BlockData {
        require(reservedNoteBlocks.size <= 384) { "Too many blocks!" }
        val block = BlockData(typeName, displayName, material, modelId, instrument, note)

        // Add to lists
        blockByNbId[block.instrument + block.note] = block
        blockByModelId[block.modelId] = block

        // Reserve Noteblocks
        val data = noteBlockForModelId(block.modelId)
        reservedNoteBlocks.add(data)

        return  block
    }

    private fun registerItem(
        typeName: String,
        displayName: String,
        material: Material,
        modelId: Int
    ): ItemData {
        val item = ItemData(typeName, displayName, material, modelId)
        return item
    }

    fun isReservedNoteblock(block: Block) = block.blockData in reservedNoteBlocks

    fun itemForNoteBlock(block: BlockState): ItemStack? {
        val (_, _, instrument, _, note) = block.blockData.asString.split('[','=',',',']') // TODO: heheheha this is very scuffed
        val nbId = instrument + note
        return blockByNbId[nbId]?.getItem(1)
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
