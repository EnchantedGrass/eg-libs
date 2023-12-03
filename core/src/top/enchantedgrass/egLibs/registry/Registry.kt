package top.enchantedgrass.egLibs.registry

/**
 * Represents a registry that can register [Registrable] objects.
 */
interface Registry<ID, T : Registrable<ID>> : MutableIterable<T> {
    /**
     * Registers [registrable] to this registry.
     * @return false if the [Registrable] object was already registered.
     */
    fun register(registrable: T): Boolean

    /**
     * Unregisters the [Registrable] object associated with [id] from this registry.
     * @return false if the [Registrable] object was not registered.
     */
    fun unregister(id: ID): Boolean

    /**
     * Finds a registrable item by its [id].
     */
    fun find(id: ID): T?

    /**
     * Retrieves a registrable item by its [id].
     */
    operator fun get(id: ID): T

    /**
     * Checks if a registrable item is in the registry by its [id].
     */
    operator fun contains(id: ID): Boolean
}

operator fun <ID, T : Registrable<ID>> Registry<ID, T>.plusAssign(registrable: T) {
    register(registrable)
}

operator fun <ID, T : Registrable<ID>> Registry<ID, T>.minusAssign(id: ID) {
    unregister(id)
}

/**
 * Creates a new [Registry].
 */
fun <ID, T : Registrable<ID>> Registry(): Registry<ID, T> = RegistryImpl()
