package top.enchantedgrass.egLibs.config

import org.slf4j.Logger
import top.enchantedgrass.egLibs.registry.Registry
import kotlin.reflect.KClass

/**
 * Represents a configuration registry.
 */
abstract class ConfigurationRegistry<C : Configuration>(delegate: Registry<C> = Registry()) : Registry<C> by delegate {
    internal abstract var logger: Logger?

    /**
     * Loads and registers a configuration with the specified [name] and [type].
     */
    fun <T : Any> load(name: String, type: KClass<T>): C {
        val config = create(name, type)
        if (register(config)) {
            logger?.info("Loaded configuration $name")
        } else {
            logger?.warn("Configuration $name is already loaded!")
        }
        return config
    }

    /**
     * Creates a new configuration with the specified [name] and [type].
     */
    protected abstract fun <T : Any> create(name: String, type: KClass<T>): C
}
