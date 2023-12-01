package top.enchantedgrass.egLibs.config

import top.enchantedgrass.egLibs.registry.Registrable
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Represents a configuration id. Used for determining the configuration path.
 */
typealias ConfigId = String

/**
 * Represents a container for a configuration object.
 */
interface Configuration : Registrable<ConfigId> {
    /**
     * Unwraps this configuration to the specified [type].
     * @throws IllegalArgumentException if this configuration is not of type [type].
     */
    fun <T : Any> unwrap(type: KClass<T>): T

    /**
     * Reloads this configuration.
     */
    fun reload()
}

inline fun <reified T : Any> Configuration.unwrap(): T = unwrap(T::class)

inline operator fun <reified T : Any> Configuration.getValue(
    @Suppress("UNUSED_PARAMETER", "KotlinRedundantDiagnosticSuppress") thisRef: Any?,
    property: KProperty<*>
): T = unwrap<T>()
