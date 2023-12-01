package top.enchantedgrass.egLibs.registry

internal class RegistryImpl<ID, T : Registrable<ID>> : Registry<ID, T> {
    private val map = mutableMapOf<ID, T>()

    override fun register(registrable: T): Boolean {
        if (registrable.id in map) return false
        map[registrable.id] = registrable
        registrable.onRegister()
        return true
    }

    override fun unregister(id: ID): Boolean {
        val registrable = map.remove(id) ?: return false
        registrable.onUnregister()
        return true
    }

    override fun find(id: ID) = map[id]

    override fun get(id: ID) = checkNotNull(map[id]) { "No registrable found for id $id" }

    override fun contains(id: ID) = id in map

    override fun iterator() = map.values.iterator()
}
