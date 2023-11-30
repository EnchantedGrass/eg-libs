package top.enchantedgrass.egLibs.registry

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import net.kyori.adventure.key.Key
import kotlin.test.*

class RegistryImplTest {
    @Test
    fun `register should add registrable to map and call onRegister`() {
        val key = Key.key("test")
        val registrable = mockk<Registrable>(relaxed = true) {
            every { key() } returns key
        }

        val registry = RegistryImpl<Registrable>()

        assertTrue(registry.register(registrable))
        verify { registrable.onRegister() }
    }

    @Test
    fun `register should not add registrable to map if it already exists`() {
        val key = Key.key("test")
        val registrable = mockk<Registrable>(relaxed = true) {
            every { key() } returns key
        }

        val registry = RegistryImpl<Registrable>()
        registry += registrable

        assertFalse(registry.register(registrable))
    }

    @Test
    fun `unregister should remove registrable from map and call onUnregister`() {
        val key = Key.key("test")
        val registrable = mockk<Registrable>(relaxed = true) {
            every { key() } returns key
        }

        val registry = RegistryImpl<Registrable>()
        registry.register(registrable)

        assertTrue(registry.unregister(key))
        verify { registrable.onUnregister() }
    }

    @Test
    fun `unregister should not remove registrable from map if it does not exist`() {
        val key = Key.key("test")
        val registry = RegistryImpl<Registrable>()

        assertFalse(registry.unregister(key))
    }

    @Test
    fun `find should return registrable if it exists`() {
        val key = Key.key("test")
        val registrable = mockk<Registrable>(relaxed = true) {
            every { key() } returns key
        }

        val registry = RegistryImpl<Registrable>()
        registry.register(registrable)

        assertNotNull(registry.find(key))
    }

    @Test
    fun `find should return null if registrable does not exist`() {
        val key = Key.key("test")
        val registry = RegistryImpl<Registrable>()

        assertNull(registry.find(key))
    }

    @Test
    fun `get should return registrable if it exists`() {
        val key = Key.key("test")
        val registrable = mockk<Registrable>(relaxed = true)
        every { registrable.key() } returns key

        val registry = RegistryImpl<Registrable>()
        registry += registrable

        assertNotNull(registry[key])
    }

    @Test
    fun `get should throw exception if registrable does not exist`() {
        val key = Key.key("test")
        val registry = RegistryImpl<Registrable>()

        assertFailsWith<IllegalStateException>("No registrable found for key $key") {
            registry[key]
        }
    }

    @Test
    fun `contains should return true if registrable exists`() {
        val key = Key.key("test")
        val registrable = mockk<Registrable>(relaxed = true) {
            every { key() } returns key
        }

        val registry = RegistryImpl<Registrable>()
        registry += registrable

        assertTrue(key in registry)
    }

    @Test
    fun `contains should return false if registrable does not exist`() {
        val key = Key.key("test")
        val registry = RegistryImpl<Registrable>()

        assertFalse(key in registry)
    }
}
