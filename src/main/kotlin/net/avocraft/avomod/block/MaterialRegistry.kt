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

    val itemByTypeName: HashBiMap<String, ItemData> = HashBiMap.create()
    private val blockByNbId = HashBiMap.create<String, BlockData>()
    private val blockByModelId = HashBiMap.create<Int, BlockData>()

    init {
        registerBlock(
            BlockData("test_block","Test Block", Material.STONE, 1, "banjo", 0),
            BlockData("accelerite_ore","Accelerite Ore", Material.END_STONE, 2, "banjo", 1)
        )

        registerItem(
            ItemData("coal_coke", "Coal Coke", Material.PAPER, 3),
            ItemData("accelerite_shard", "Accelerite Shard", Material.PAPER, 4),
            ItemData("accelerite_ingot", "Accelerite Ingot", Material.PAPER, 5)
        )
    }

    private fun registerBlock(vararg blocks: BlockData) {
        require(blocks.size <= 384) { "Too many blocks!" }

        for (block in blocks) {
            // Add to lists
            itemByTypeName[block.typeName] = block.asItem()
            blockByNbId[block.instrument + block.note] = block
            blockByModelId[block.modelId] = block

            // Reserve Noteblocks
            val data = noteBlockForModelId(block.modelId)
            reservedNoteBlocks.add(data)

        }
    }

    private fun registerItem(vararg items: ItemData) {
        for (item in items) {
            // Add to lists
            itemByTypeName[item.typeName] = item
        }
    }

    fun isReservedNoteblock(block: Block) = block.blockData in reservedNoteBlocks

    fun itemForTypeName(typeName: String, amount: Int): ItemStack {
        return itemByTypeName[typeName]?.getItem(amount)!!
    }

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
