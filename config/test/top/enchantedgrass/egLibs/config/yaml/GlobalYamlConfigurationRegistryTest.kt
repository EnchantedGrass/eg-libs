package top.enchantedgrass.egLibs.config.yaml

import kotlin.test.Test
import kotlin.test.assertFailsWith

class GlobalYamlConfigurationRegistryTest {
    @Test
    fun `should throw exception when already initialized`() {
        GlobalYamlConfigurationRegistry.init { }
        assertFailsWith<IllegalStateException> {
            GlobalYamlConfigurationRegistry.init { }
        }
    }
}
