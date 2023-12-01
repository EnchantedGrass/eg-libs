package top.enchantedgrass.egLibs.config

import org.slf4j.Logger
import top.enchantedgrass.egLibs.registry.Registry
import kotlin.reflect.KClass

/**
 * Represents a configuration registry.
 */
abstract class AbstractConfigurationRegistry<C : Configuration>(
    private val delegate: Registry<ConfigId, C> = Registry(),
) : ConfigurationRegistry<C> {
    internal abstract val logger: Logger?

    /**
     * Creates a new configuration with the specified [id] and [type].
     */
    protected abstract fun <T : Any> create(id: ConfigId, type: KClass<T>): C

    /**
     * Loads and registers a configuration with the specified [id] and [type].
     * If the configuration is already loaded, it will return the loaded configuration.
     */
    override fun <T : Any> load(id: ConfigId, type: KClass<T>): C {
        if (id in delegate) {
            logger?.warn("Configuration $id is already loaded!")
            return delegate[id]
        }
        val config = create(id, type)
        require(register(config))
        logger?.info("Loaded configuration $id")
        return config
    }

    override fun iterator() = delegate.iterator()

    override fun register(registrable: C) = delegate.register(registrable)

    override fun unregister(id: ConfigId) = delegate.unregister(id)

    override fun find(id: ConfigId): C? = delegate.find(id)

    override fun get(id: ConfigId): C = delegate[id]

    override fun contains(id: ConfigId) = id in delegate
}
