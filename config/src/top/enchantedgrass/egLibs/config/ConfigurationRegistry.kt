package top.enchantedgrass.egLibs.config

import top.enchantedgrass.egLibs.registry.Registry
import kotlin.reflect.KClass

/**
 * Represents a configuration registry.
 */
interface ConfigurationRegistry<C : Configuration> : Registry<ConfigId, C> {
    /**
     * Loads and registers a configuration with the specified [id] and [type].
     */
    fun <T : Any> load(id: ConfigId, type: KClass<T>): C
}
