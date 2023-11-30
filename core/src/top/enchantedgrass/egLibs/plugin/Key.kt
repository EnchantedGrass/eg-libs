package top.enchantedgrass.egLibs.plugin

import net.kyori.adventure.key.Key

/**
 * Creates new [Key] associated with [EgPlugin] plugin namespace and [value].
 */
fun EgPlugin.newKey(value: String) = Key.key(this, value)
