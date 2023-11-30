package top.enchantedgrass.egLibs.plugin

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import top.enchantedgrass.egLibs.fixtures.plugin.mockkEgJavaPlugin
import kotlin.test.assertEquals

class KeyKtTest {
    @Test
    fun `newKey() should create new Key associated with plugin namespace`() {
        val mockk = mockkEgJavaPlugin {
            every { namespace() } returns "test-plugin"
        }
        val key = mockk.newKey("test-key")

        assertEquals("test-plugin", key.namespace())
        assertEquals("test-plugin:test-key", key.toString())

        verify { mockk.namespace() }
        confirmVerified(mockk)
    }
}
