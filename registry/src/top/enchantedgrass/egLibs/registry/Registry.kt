package top.enchantedgrass.egLibs.registry

import net.kyori.adventure.key.Key

/**
 * Represents a registry that can register [Registrable] objects.
 */
interface Registry<T : Registrable> : MutableIterable<T> {
    /**
     * Registers [registrable] to this registry.
     * @return false if the [Registrable] object was already registered.
     */
    fun register(registrable: T): Boolean

    /**
     * Unregisters the [Registrable] object associated with [key] from this registry.
     * @return false if the [Registrable] object was not registered.
     */
    fun unregister(key: Key): Boolean

    /**
     * Finds a registrable item by its [key].
     */
    fun find(key: Key): T?

    /**
     * Retrieves a registrable item by its [key].
     */
    operator fun get(key: Key): T

    /**
     * Checks if a registrable item is in the registry by its [key].
     */
    operator fun contains(key: Key): Boolean
}

operator fun <T : Registrable> Registry<T>.plusAssign(registrable: T) {
    register(registrable)
}

operator fun <T : Registrable> Registry<T>.minusAssign(key: Key) {
    unregister(key)
}

/**
 * Creates a new [Registry].
 */
fun <T : Registrable> Registry(): Registry<T> = RegistryImpl()
