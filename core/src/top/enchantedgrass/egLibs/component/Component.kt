@file:Suppress("unused")

package top.enchantedgrass.egLibs.component

import de.themoep.minedown.adventure.MineDown
import net.kyori.adventure.text.Component

/**
 * Deserializes this string to a component using [MineDown].
 * The placeholders are replaced using [placeholders].
 *
 * @see MineDown
 * @see Placeholder
 */
fun String.mdc(vararg placeholders: Placeholder): Component = mdc(placeholders.toList())

/**
 * Deserializes this string to a component using [MineDown].
 * The placeholders are replaced using [placeholders].
 *
 * @see MineDown
 * @see Placeholder
 */
fun String.mdc(placeholders: List<Placeholder>): Component =
    MineDown(this).replaceFirst(true).replace(placeholders.toMap()).toComponent()

/**
 * Deserializes this string to a list component using [MineDown].
 * The placeholders are replaced using [placeholders].
 *
 * @see MineDown
 * @see Placeholder
 */
fun String.mdcList(vararg placeholders: Placeholder): List<Component> = mdcList(placeholders.toList())

/**
 * Deserializes this string to a list component using [MineDown].
 * The placeholders are replaced using [placeholders].
 *
 * @see MineDown
 * @see Placeholder
 */
fun String.mdcList(placeholders: List<Placeholder>): List<Component> = split("\n").mdc(placeholders)

/**
 * Deserializes this list of strings to a list of components using [MineDown].
 * The placeholders are replaced using [placeholders].
 *
 * @see MineDown
 * @see Placeholder
 */
fun List<String>.mdc(vararg placeholders: Placeholder): List<Component> = mdc(placeholders.toList())

/**
 * Deserializes this list of strings to a list of components using [MineDown].
 * The placeholders are replaced using [placeholders].
 *
 * @see MineDown
 * @see Placeholder
 */
fun List<String>.mdc(placeholders: List<Placeholder>): List<Component> = map { it.mdc(placeholders) }
