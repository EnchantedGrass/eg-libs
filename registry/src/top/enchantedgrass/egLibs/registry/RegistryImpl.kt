package top.enchantedgrass.egLibs.registry

import net.kyori.adventure.key.Key

internal class RegistryImpl<T : Registrable> : Registry<T> {
    private val map = mutableMapOf<Key, T>()

    override fun register(registrable: T): Boolean {
        if (registrable.key() in map) return false
        map[registrable.key()] = registrable
        registrable.onRegister()
        return true
    }

    override fun unregister(key: Key): Boolean {
        val registrable = map.remove(key) ?: return false
        registrable.onUnregister()
        return true
    }

    override fun find(key: Key) = map[key]

    override fun get(key: Key) = checkNotNull(map[key]) { "No registrable found for key $key" }

    override fun contains(key: Key) = key in map

    override fun iterator() = map.values.iterator()
}
