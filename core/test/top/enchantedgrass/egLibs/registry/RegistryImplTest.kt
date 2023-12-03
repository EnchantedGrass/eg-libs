package top.enchantedgrass.egLibs.registry

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.*

class RegistryImplTest {
    @Test
    fun `register should add registrable to map and call onRegister`() {
        val testId = "test"
        val registrable = mockk<Registrable<String>>(relaxed = true) {
            every { id } returns testId
        }

        val registry = RegistryImpl<String, Registrable<String>>()

        assertTrue(registry.register(registrable))
        verify { registrable.onRegister() }
    }

    @Test
    fun `register should not add registrable to map if it already exists`() {
        val testId = "test"
        val registrable = mockk<Registrable<String>>(relaxed = true) {
            every { id } returns testId
        }

        val registry = RegistryImpl<String, Registrable<String>>()
        registry += registrable

        assertFalse(registry.register(registrable))
    }

    @Test
    fun `unregister should remove registrable from map and call onUnregister`() {
        val testId = "test"
        val registrable = mockk<Registrable<String>>(relaxed = true) {
            every { id } returns testId
        }

        val registry = RegistryImpl<String, Registrable<String>>()
        registry.register(registrable)

        assertTrue(registry.unregister(testId))
        verify { registrable.onUnregister() }
    }

    @Test
    fun `unregister should not remove registrable from map if it does not exist`() {
        val testId = "test"
        val registry = RegistryImpl<String, Registrable<String>>()

        assertFalse(registry.unregister(testId))
    }

    @Test
    fun `find should return registrable if it exists`() {
        val testId = "test"
        val registrable = mockk<Registrable<String>>(relaxed = true) {
            every { id } returns testId
        }

        val registry = RegistryImpl<String, Registrable<String>>()
        registry.register(registrable)

        assertNotNull(registry.find(testId))
    }

    @Test
    fun `find should return null if registrable does not exist`() {
        val testId = "test"
        val registry = RegistryImpl<String, Registrable<String>>()

        assertNull(registry.find(testId))
    }

    @Test
    fun `get should return registrable if it exists`() {
        val testId = "test"
        val registrable = mockk<Registrable<String>>(relaxed = true) {
            every { id } returns testId
        }

        val registry = RegistryImpl<String, Registrable<String>>()
        registry += registrable

        assertNotNull(registry[testId])
    }

    @Test
    fun `get should throw exception if registrable does not exist`() {
        val testId = "test"
        val registry = RegistryImpl<String, Registrable<String>>()

        assertFailsWith<IllegalStateException>("No registrable found for id $testId") {
            registry[testId]
        }
    }

    @Test
    fun `contains should return true if registrable exists`() {
        val testId = "test"
        val registrable = mockk<Registrable<String>>(relaxed = true) {
            every { id } returns testId
        }

        val registry = RegistryImpl<String, Registrable<String>>()
        registry += registrable

        assertTrue(testId in registry)
    }

    @Test
    fun `contains should return false if registrable does not exist`() {
        val testId = "test"
        val registry = RegistryImpl<String, Registrable<String>>()

        assertFalse(testId in registry)
    }
}
