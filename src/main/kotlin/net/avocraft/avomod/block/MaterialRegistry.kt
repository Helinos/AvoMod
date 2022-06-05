package net.avocraft.avomod.block

import com.google.common.collect.HashBiMap
import com.google.common.collect.HashMultimap
import com.google.common.collect.Multimap
import it.unimi.dsi.fastutil.Hash
import net.avocraft.avomod.Logger
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.block.Block
import org.bukkit.block.BlockState
import org.bukkit.block.data.type.NoteBlock
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.HashSet

/**
 * @author bush
 * @since 5/29/2022
 */
object MaterialRegistry {
    private val reservedNoteBlocks = HashSet<NoteBlock>()

    val itemByTypeName: HashBiMap<String, AvoItem> = HashBiMap.create()
    private val blockByNbId = HashBiMap.create<String, AvoBlock>()
    private val blockByModelId = HashBiMap.create<Int, AvoBlock>()

    // Blocks
    val TEST_BLOCK = registerBlock("test_block", "Test Block", Material.STONE, 1, "banjo", 0)
    val ACCELERITE_ORE = registerBlock("accelerite_ore", "Accelerite Ore", Material.END_STONE, 2, "banjo", 1)

    // Default Items
    val COAL_COKE = registerItem("coal_coke", "Coal Coke", 3)
    val ACCELERITE_SHARD = registerItem("accelerite_shard", "Accelerite Shard", 4)
    val ACCELERITE_INGOT = registerItem("accelerite_ingot", "Accelerite Ingot", 5)

    // Foods
    val BERRY_PIE = registerItem("berry_pie", "Berry Pie", 10, Material.PUMPKIN_PIE)
    val FRIED_EGG = registerItem("fried_egg", "Fried Egg", 11, Material.COOKED_CHICKEN)
    val HONEYED_APPLE = registerItem("honeyed_apple", "Honeyed Apple", 12, Material.COOKED_PORKCHOP)
    val CHICKEN_NUGGET = registerItem("chicken_nugget", "Chicken Nugget", 13, Material.COOKED_RABBIT)

    // Items with attributes (Armor, tools)
    val ACCELERITE_HELMET = registerItem(
        "accelerite_helmet",
        "Accelerite Helmet",
        6,
        Material.LEATHER_HELMET,
        HashMultimap.create<Attribute, AttributeModifier>().apply {
            this.put(
                Attribute.GENERIC_ARMOR,
                AttributeModifier(UUID.randomUUID(),"generic.armor", 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD)
            )
            this.put(
                Attribute.GENERIC_ARMOR_TOUGHNESS,
                AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HEAD)
            )
        }
    )
    val ACCELERITE_CHESTPLATE =
        registerItem("accelerite_chestplate", "Accelerite Chestplate", 7, Material.LEATHER_CHESTPLATE,
            HashMultimap.create<Attribute, AttributeModifier>().apply {
                this.put(
                    Attribute.GENERIC_ARMOR,
                    AttributeModifier(UUID.randomUUID(),"generic.armor", 8.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST)
                )
                this.put(
                    Attribute.GENERIC_ARMOR_TOUGHNESS,
                    AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.CHEST)
                )
            }
        )
    val ACCELERITE_LEGGINGS = registerItem("accelerite_leggings", "Accelerite Leggings", 8, Material.LEATHER_LEGGINGS,
        HashMultimap.create<Attribute, AttributeModifier>().apply {
            this.put(
                Attribute.GENERIC_ARMOR,
                AttributeModifier(UUID.randomUUID(),"generic.armor", 6.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS)
            )
            this.put(
                Attribute.GENERIC_ARMOR_TOUGHNESS,
                AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.LEGS)
            )
        }
    )
    val ACCELERITE_BOOTS = registerItem("accelerite_boots", "Accelerite Boots", 9, Material.LEATHER_BOOTS,
        HashMultimap.create<Attribute, AttributeModifier>().apply {
            this.put(
                Attribute.GENERIC_ARMOR,
                AttributeModifier(UUID.randomUUID(),"generic.armor", 3.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET)
            )
            this.put(
                Attribute.GENERIC_ARMOR_TOUGHNESS,
                AttributeModifier(UUID.randomUUID(), "generic.armor_toughness", 1.0, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.FEET)
            )
        }
    )

    private fun registerBlock(
        typeName: String,
        displayName: String,
        material: Material,
        modelId: Int,
        instrument: String,
        note: Int,
    ): AvoBlock {
        require(reservedNoteBlocks.size <= 384) { "Too many blocks!" }
        val block = AvoBlock("avocraft:$typeName", displayName, material, modelId, instrument, note)

        // Add to lists
        itemByTypeName[block.typeName] = block.asAvoItem()
        blockByNbId[block.instrument + block.note] = block
        blockByModelId[block.modelId] = block

        // Reserve Noteblocks
        val data = noteBlockForModelId(block.modelId)
        reservedNoteBlocks.add(data)

        return block
    }

    private fun registerItem(
        typeName: String,
        displayName: String,
        modelId: Int,
        material: Material = Material.PAPER,
        // hasAttributeModifiers: Boolean = false,
        attributeModifiers: HashMultimap<Attribute, AttributeModifier>? = null
    ): AvoItem {
        val item = AvoItem("avocraft:$typeName", displayName, material, modelId, attributeModifiers)

        // Add to lists
        itemByTypeName[item.typeName] = item

        return item
    }

    fun isReservedNoteBlock(block: Block) = block.blockData in reservedNoteBlocks

    fun itemForTypeName(typeName: String, amount: Int): ItemStack {
        return itemByTypeName[typeName]!!.item(amount)
    }

    fun itemForNoteBlock(block: BlockState): ItemStack? {
        val (_, _, instrument, _, note) = block.blockData.asString.split(
            '[',
            '=',
            ',',
            ']'
        ) // TODO: heheheha this is very scuffed
        val nbId = instrument + note
        return blockByNbId[nbId]?.item()
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
