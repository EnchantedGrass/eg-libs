package top.enchantedgrass.egLibs.config.yaml

import io.mockk.*
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockKExtension::class)
class YamlConfigurationRegistryTest {
    @SpyK
    private var registry: YamlConfigurationRegistry = YamlConfigurationRegistry()

    @BeforeEach
    fun setUp() {
        val nameSlot = slot<String>()
        every { createMethod.invoke(registry, capture(nameSlot), TestConfigObject::class) } answers {
            spyk(YamlConfiguration(registry, nameSlot.captured, TestConfigObject::class)) {
                justRun { onRegister() }
                justRun { onUnregister() }
            }
        }
    }

    @Test
    fun `should load and register configuration with specified name and type`() {
        val loaded = registry.load("test", TestConfigObject::class)
        verify(exactly = 1) { loaded.onRegister() }

        val config = registry.find("test")

        assertEquals(loaded, config)
        assertNotNull(config)
    }

    @Test
    fun `should not load configuration with same name twice`() {
        val loaded1 = registry.load("test", TestConfigObject::class)
        val loaded2 = registry.load("test", TestConfigObject::class)

        assertEquals(loaded1, loaded2)
        verify(exactly = 1) { loaded1.onRegister() }
    }

    @Test
    fun `should call onUnregister when unregistering`() {
        val loaded = registry.load("test", TestConfigObject::class)
        registry.unregister("test")

        verify(exactly = 1) { loaded.onUnregister() }
    }

    companion object {
        private val createMethod = YamlConfigurationRegistry::class.java.getDeclaredMethod(
            "create",
            String::class.java,
            KClass::class.java,
        )

        init {
            createMethod.isAccessible = true
        }
    }
}
