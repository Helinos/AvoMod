package net.avocraft.avomod

import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

object CommandGive : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender is Player) {
            val player = sender.player

            val testBlock = ItemStack(Material.STONE,)
            val itemMeta = testBlock.itemMeta
            itemMeta?.setCustomModelData(1)
            itemMeta?.setDisplayName("§rTest Block")
            testBlock.itemMeta = itemMeta

            player?.inventory?.addItem(testBlock)
        }

        return true
    }

}