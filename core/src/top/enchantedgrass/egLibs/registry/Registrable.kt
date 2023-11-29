package top.enchantedgrass.egLibs.registry

import net.kyori.adventure.key.Keyed

/**
 * Represents an object that can be registered to a [Registry].
 */
interface Registrable : Keyed {
    /**
     * Invokes when this object is registered to a [Registry].
     */
    fun onRegister() = Unit

    /**
     * Invokes when this object is unregistered from a [Registry].
     */
    fun onUnregister() = Unit
}
