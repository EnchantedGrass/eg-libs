@file:Suppress("unused")

package top.enchantedgrass.egLibs.component

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

/**
 * A placeholder is a pair of a key and a value used to replace a string when deserializing it to a component.
 *
 * A placeholder key is a string between two curly `%`, like `%player_name%`.
 * A placeholder value is any object.
 */
typealias Placeholder = Pair<String, Any>

/**
 * Returns common placeholders for this player.
 *
 * List of placeholders:
 * - `player_name`: player name
 * - `player_displayName`: player display name
 */
fun Player.commonPlaceholders(): List<Placeholder> = (this as CommandSender).commonPlaceholders() + listOf(
    "player_name" to name,
    "player_displayName" to displayName(),
)

/**
 * Returns target placeholders for this player.
 *
 * List of placeholders:
 * - `target_name`: target name
 * - `target_displayName`: target display name
 */
fun Player.targetPlaceholders(): List<Placeholder> = (this as CommandSender).targetPlaceholders() + listOf(
    "target_name" to name,
    "target_displayName" to displayName(),
)

/**
 * Returns common placeholders for this command sender.
 *
 * List of placeholders:
 * - `sender_name`: sender name
 */
fun CommandSender.commonPlaceholders(): List<Placeholder> = listOf("sender_name" to name)

/**
 * Returns target placeholders for this command sender.
 *
 * List of placeholders:
 * - `target_name`: target name
 */
fun CommandSender.targetPlaceholders(): List<Placeholder> = listOf("target_name" to name)
