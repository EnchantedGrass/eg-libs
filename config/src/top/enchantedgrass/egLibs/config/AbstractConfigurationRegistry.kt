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
     */
    override fun <T : Any> load(id: ConfigId, type: KClass<T>): C {
        val config = create(id, type)
        if (register(config)) {
            logger?.info("Loaded configuration $id")
        } else {
            logger?.warn("Configuration $id is already loaded!")
        }
        return config
    }

    override fun iterator() = delegate.iterator()

    override fun register(registrable: C) = delegate.register(registrable)

    override fun unregister(id: ConfigId) = delegate.unregister(id)

    override fun find(id: ConfigId): C? = delegate.find(id)

    override fun get(id: ConfigId): C = delegate[id]

    override fun contains(id: ConfigId) = id in delegate
}
