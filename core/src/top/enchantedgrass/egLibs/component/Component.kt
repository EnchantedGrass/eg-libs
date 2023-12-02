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
 * Replaces the placeholders in this string with the provided values and converts it into a list of components
 * using the [MineDown] library.
 *
 * @param placeholders The list of placeholders to be replaced in the string.
 * @param replaceFirst Indicates should replace string placeholders before splitting the string.
 * @return The resulting list of components after replacing the placeholders.
 *
 * @see MineDown
 * @see Placeholder
 */
fun String.mdcList(vararg placeholders: Placeholder, replaceFirst: Boolean = false): List<Component> =
    mdcList(placeholders.toList(), replaceFirst)

/**
 * Replaces the placeholders in this string with the provided values and converts it into a list of components
 * using the [MineDown] library.
 *
 * @param placeholders The list of placeholders to be replaced in the string.
 * @param replaceFirst Indicates should replace string placeholders before splitting the string.
 * @return The resulting list of components after replacing the placeholders.
 *
 * @see MineDown
 * @see Placeholder
 */
fun String.mdcList(placeholders: List<Placeholder>, replaceFirst: Boolean = false): List<Component> {
    var result = this
    val remainingPlaceholders = placeholders.toMutableList()
    if (replaceFirst) {
        for (placeholder in placeholders) {
            val (key, value) = placeholder
            if (value !is String) {
                continue
            }

            result = result.replace(key, value)
            remainingPlaceholders -= placeholder
        }
    }
    return result.split("\n").mdc(remainingPlaceholders)
}

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
