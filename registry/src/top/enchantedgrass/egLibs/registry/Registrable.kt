package top.enchantedgrass.egLibs.registry

/**
 * Represents an object that can be registered to a [Registry].
 */
interface Registrable<ID> {
    /**
     * The object's id.
     */
    val id: ID

    /**
     * Invokes when this object is registered to a [Registry].
     */
    fun onRegister() = Unit

    /**
     * Invokes when this object is unregistered from a [Registry].
     */
    fun onUnregister() = Unit
}
