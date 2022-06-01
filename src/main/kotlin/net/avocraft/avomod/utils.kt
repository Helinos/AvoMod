package net.avocraft.avomod

import org.bukkit.inventory.ItemStack

fun String.toID() = lowercase().replace(' ', '_')

fun ItemStack.withCount(count: Int) = apply { amount = count }

fun ItemStack.metaNameOrDefault() = itemMeta?.displayName?.toID() ?: type.name
