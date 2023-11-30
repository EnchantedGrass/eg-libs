package top.enchantedgrass.egLibs.plugin

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.verifyAll
import org.junit.jupiter.api.Test
import top.enchantedgrass.egLibs.fixtures.plugin.mockkEgJavaPlugin
import kotlin.test.assertEquals

class EgJavaPluginTest {
    @Test
    fun `namespace() should return plugin name in lowercase`() {
        val mockk = mockkEgJavaPlugin {
            every { name } returns "TestPlugin"
            every { namespace() } answers { callOriginal() }
        }

        assertEquals("testplugin", mockk.namespace())
        verifyAll {
            mockk.name
            mockk.namespace()
        }
        confirmVerified(mockk)
    }
}
