package top.enchantedgrass.egLibs.config

import net.kyori.adventure.key.Key
import org.slf4j.Logger
import top.enchantedgrass.egLibs.registry.Registry
import kotlin.reflect.KClass

/**
 * Represents a configuration registry.
 */
abstract class AbstractConfigurationRegistry<C : Configuration>(private val delegate: Registry<C> = Registry()) :
    ConfigurationRegistry<C> {
    internal abstract val logger: Logger?

    /**
     * Creates a new configuration with the specified [name] and [type].
     */
    protected abstract fun <T : Any> create(name: String, type: KClass<T>): C

    /**
     * Loads and registers a configuration with the specified [name] and [type].
     */
    override fun <T : Any> load(name: String, type: KClass<T>): C {
        val config = create(name, type)
        if (register(config)) {
            logger?.info("Loaded configuration $name")
        } else {
            logger?.warn("Configuration $name is already loaded!")
        }
        return config
    }

    override fun iterator() = delegate.iterator()

    override fun register(registrable: C) = delegate.register(registrable)

    override fun unregister(key: Key) = delegate.unregister(key)

    override fun find(key: Key): C? = delegate.find(key)

    override fun get(key: Key): C = delegate[key]

    override fun contains(key: Key) = key in delegate
}
