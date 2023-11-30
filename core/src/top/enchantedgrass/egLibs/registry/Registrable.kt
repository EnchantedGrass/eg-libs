package top.enchantedgrass.egLibs.registry

import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Keyed

/**
 * Represents an object that can be registered to a [Registry].
 */
interface Registrable : Keyed {
    /**
     * The key of this object.
     */
    val key: Key

    override fun key(): Key = key

    /**
     * Invokes when this object is registered to a [Registry].
     */
    fun onRegister() = Unit

    /**
     * Invokes when this object is unregistered from a [Registry].
     */
    fun onUnregister() = Unit
}
