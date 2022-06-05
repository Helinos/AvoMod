package net.avocraft.avomod

import net.avocraft.avomod.block.MaterialRegistry
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import java.nio.file.FileSystemNotFoundException
import java.nio.file.FileSystems
import kotlin.io.path.toPath

fun String.toID() = lowercase().replace(' ', '_')

fun ItemStack.metaNameOrDefault() = itemMeta?.displayName?.toID() ?: type.name

// https://stackoverflow.com/a/67839914/18210688
fun Any.getResource(folder: String) = this::class.java.getResource(folder)!!.toURI().let { uri ->
    try {
        // Running in debug/ide
        uri.toPath()
    } catch (exception: FileSystemNotFoundException) {
        // Running in jar
        FileSystems.newFileSystem(uri, mutableMapOf<String, String>()).getPath(folder)
    }
}

// This will throw an NPE if an item isn't valid
fun stackOf(rawName: String, count: Int = 1): ItemStack {
    val name = rawName.removeSurrounding("\"")
//    try {
    return when {
        name.startsWith("minecraft:") -> ItemStack(
            Material.getMaterial(
                name.removePrefix("minecraft:").uppercase()
            )!!, count
        )
        name.startsWith("avocraft:") -> MaterialRegistry.itemForTypeName(name, count)
        else -> stackOf("minecraft:$name", count)
    }
//    } catch (e: NullPointerException) {
//        throw Exception("$name is not a valid item!") // This may make it hard to know exactly which recipe caused this
//    }
}