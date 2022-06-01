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

    // Blocks
    val TEST_BLOCK = registerBlock("test_block", "Test Block", Material.STONE, 1, "banjo", 0)
    val ACCELERITE_ORE = registerBlock("accelerite_ore", "Accelerite Ore", Material.END_STONE, 2, "banjo", 1)

    // Default Items
    val COAL_COKE = registerDefaultItem("coal_coke", "Coal Coke", 3)
    val ACCELERITE_SHARD = registerDefaultItem("accelerite_shard", "Accelerite Shard", 4)
    val ACCELERITE_INGOT = registerDefaultItem("accelerite_ingot", "Accelerite Ingot", 5)

    // Armor, Tools and Foods
    val ACCELERITE_HELMET = registerItem("accelerite_helmet", "Accelerite Helmet", Material.LEATHER_HELMET, 6)
    val ACCELERITE_CHESTPLATE =
        registerItem("accelerite_chestplate", "Accelerite Chestplate", Material.LEATHER_CHESTPLATE, 7)
    val ACCELERITE_LEGGINGS = registerItem("accelerite_leggings", "Accelerite Leggings", Material.LEATHER_LEGGINGS, 8)
    val ACCELERITE_BOOTS = registerItem("accelerite_boots", "Accelerite Boots", Material.LEATHER_BOOTS, 9)
    val BERRY_PIE = registerItem("berry_pie", "Berry Pie", Material.PUMPKIN_PIE, 10)
    val FRIED_EGG = registerItem("fried_egg", "Fried Egg", Material.COOKED_CHICKEN, 11)
    val HONEYED_APPLE = registerItem("honeyed_apple", "Honeyed Apple", Material.COOKED_PORKCHOP, 12)
    val CHICKEN_NUGGET = registerItem("chicken_nugget", "Chicken Nugget", Material.COOKED_RABBIT, 13)


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
        itemByTypeName[block.typeName] = block.asItem()
        blockByNbId[block.instrument + block.note] = block
        blockByModelId[block.modelId] = block

        // Reserve Noteblocks
        val data = noteBlockForModelId(block.modelId)
        reservedNoteBlocks.add(data)

        return block
    }

    private fun registerDefaultItem(
        typeName: String,
        displayName: String,
        modelId: Int
    ): ItemData {
        return registerItem(typeName, displayName, Material.PAPER, modelId)
    }

    private fun registerItem(
        typeName: String,
        displayName: String,
        material: Material,
        modelId: Int
    ): ItemData {
        val item = ItemData(typeName, displayName, material, modelId)
        // Add to lists
        itemByTypeName[item.typeName] = item

        return item
    }

    fun isReservedNoteBlock(block: Block) = block.blockData in reservedNoteBlocks

    fun itemForTypeName(typeName: String, amount: Int): ItemStack {
        return itemByTypeName[typeName]?.getItem(amount)!!
    }

    fun itemForNoteBlock(block: BlockState): ItemStack? {
        val (_, _, instrument, _, note) = block.blockData.asString.split(
            '[',
            '=',
            ',',
            ']'
        ) // TODO: heheheha this is very scuffed
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
