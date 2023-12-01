@file:Suppress("unused")

package top.enchantedgrass.egLibs.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer

/**
 * Returns this component or an empty component if this component is null.
 * @see Component.empty
 */
val Component?.orEmpty get() = this ?: Component.empty()

/**
 * Serializes this component to a legacy string using [LegacyComponentSerializer.legacySection].
 * @see LegacyComponentSerializer.legacySection
 */
val Component?.legacyOrEmpty get() = this?.let { LegacyComponentSerializer.legacySection().serialize(it) } ?: ""

/**
 * Joins components using the configuration in [builder].
 */
fun Iterable<Component>.join(builder: JoinConfiguration.Builder.() -> Unit = { }) =
    join(JoinConfiguration.builder().apply(builder).build())

/**
 * Joins components using the configuration in [config].
 */
fun Iterable<Component>.join(config: JoinConfiguration = JoinConfiguration.newlines()) =
    Component.join(config, this)
