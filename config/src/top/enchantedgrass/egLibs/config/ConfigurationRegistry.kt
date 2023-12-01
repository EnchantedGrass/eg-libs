package top.enchantedgrass.egLibs.config

import top.enchantedgrass.egLibs.registry.Registry
import kotlin.reflect.KClass

/**
 * Represents a configuration registry.
 */
interface ConfigurationRegistry<C : Configuration> : Registry<C> {
    /**
     * Loads and registers a configuration with the specified [name] and [type].
     */
    fun <T : Any> load(name: String, type: KClass<T>): C
}
