package net.avocraft.avomod.block

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

enum class ToolLevel(val level: Int) {
    AIR(0),
    WOOD(1),
    STONE(2),
    IRON(3),
    DIAMOND(4),
    NETHERITE(5);
}

enum class ToolType {
    AIR,
    HOE,
    AXE,
    SHOVEL,
    PICKAXE,
    SWORD;
}

/**
 * @author bush
 * @since 5/29/2022
 */
class AvoBlock(
    private val typeName: String,
    private val displayName: String,
    private val material: Material,
    private val modelId: Int,
    val instrument: String,
    val note: Int,
    val hardness: Float = 1.0f,
    private val validToolType: ToolType = ToolType.AIR,
    private val minToolLevel: ToolLevel = ToolLevel.AIR,
) {
    fun item(amount: Int = 1) = ItemStack(material, amount).apply {
        val iM = this.itemMeta!!
        iM.setCustomModelData(modelId)
        iM.setDisplayName("§r$displayName")
        this.itemMeta = iM
    }

    fun asAvoItem(): AvoItem {
        return AvoItem(typeName, displayName, material, modelId)
    }

    fun isValidTool(tool: ItemStack): Boolean {
        if (validToolType == ToolType.AIR && minToolLevel == ToolLevel.AIR) {
            return true
        }

        var isValid = true

        // Level check
        val toolName = tool.type.name
        val level = when {
            toolName.contains("wooden") -> ToolLevel.WOOD
            toolName.contains("stone") -> ToolLevel.STONE
            toolName.contains("iron") -> ToolLevel.IRON
            toolName.contains("diamond") -> ToolLevel.DIAMOND
            toolName.contains("netherite") -> ToolLevel.NETHERITE
            else -> throw Exception("Block $typeName has invalid tool requirements!")
        }

        // Type check
        val type = when {
            toolName.contains("hoe") -> ToolType.HOE
            toolName.contains("axe") -> ToolType.AXE
            toolName.contains("shovel") -> ToolType.SHOVEL
            toolName.contains("pickaxe") -> ToolType.PICKAXE
            toolName.contains("sword") -> ToolType.SWORD
            else -> throw Exception("Block $typeName has invalid tool requirements!")
        }
        isValid = (minToolLevel.level <= level.level) && (validToolType == type)

        return isValid
    }

    fun getBreakSpeed(player: Player): Float {
        TODO()
    }
}

