package top.enchantedgrass.egLibs.config.yaml

import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.io.TempDir
import org.slf4j.LoggerFactory
import top.enchantedgrass.egLibs.config.getValue
import top.enchantedgrass.egLibs.config.unwrap
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.inputStream
import kotlin.io.path.writeText
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class YamlConfigurationTest {
    private var randomConfigName: String = ""
    private lateinit var configuration: YamlConfiguration

    @BeforeTest
    fun setup() {
        randomConfigName = "test${(Math.random() * 1000).toInt()}.yml"
        configuration = YamlConfiguration(registry, randomConfigName, TestConfigObject::class)
    }

    @Test
    fun `unregistered configuration should throw error when unwrapped`() {
        assertFailsWith<IllegalStateException> { configuration.unwrap<TestConfigObject>() }
    }

    @Test
    fun `should load empty configuration if current not exists`() {
        configuration.onRegister()
        val config: TestConfigObject by configuration
        assertEquals(TestConfigObject(null, null, null), config)
    }

    @Test
    fun `wrong unwarp type should throw error`() {
        configuration.onRegister()
        assertFailsWith<IllegalArgumentException> { configuration.unwrap<String>() }
    }

    @Test
    fun `should load default configuration if current not exists`() {
        val defaultConfig = TestConfigObject(1, "Hello", true, listOf("3", "4"), mapOf("a" to 6, "b" to 8))
        val defaultConfigString = registry.mapper.writeValueAsString(defaultConfig)
        defaultResourcesDirectory.resolve(randomConfigName).writeText(defaultConfigString)

        configuration.onRegister()

        val config: TestConfigObject by configuration
        assertEquals(defaultConfig, config)
    }

    @Test
    fun `should not overide existing configuration with default`() {
        val config = TestConfigObject(1, "Hello", true, listOf("3", "4"), mapOf("a" to 6, "b" to 8))
        val configString = registry.mapper.writeValueAsString(config)
        tempDataDirectory.resolve(randomConfigName).writeText(configString)

        val defaultConfig = TestConfigObject(2, "World", false, listOf("5", "6"), mapOf("c" to 7, "d" to 9))
        val defaultConfigString = registry.mapper.writeValueAsString(defaultConfig)
        defaultResourcesDirectory.resolve(randomConfigName).writeText(defaultConfigString)

        configuration.onRegister()

        val loadedConfig: TestConfigObject by configuration
        assertEquals(config, loadedConfig)
    }

    @Test
    fun `should load configuration from file`() {
        val config = TestConfigObject(1, "Hello", true, listOf("3", "4"), mapOf("a" to 6, "b" to 8))
        val configString = registry.mapper.writeValueAsString(config)
        tempDataDirectory.resolve(randomConfigName).writeText(configString)

        configuration.onRegister()

        val loadedConfig: TestConfigObject by configuration
        assertEquals(config, loadedConfig)
    }

    @Test
    fun `should reload configuration`() {
        val config = TestConfigObject(1, "Hello", true, listOf("3", "4"), mapOf("a" to 6, "b" to 8))
        val configString = registry.mapper.writeValueAsString(config)
        tempDataDirectory.resolve(randomConfigName).writeText(configString)

        configuration.onRegister()

        val loadedConfig: TestConfigObject by configuration
        assertEquals(config, loadedConfig)

        val newConfig = TestConfigObject(2, "World", false, listOf("5", "6"), mapOf("c" to 7, "d" to 9))
        val newConfigString = registry.mapper.writeValueAsString(newConfig)
        tempDataDirectory.resolve(randomConfigName).writeText(newConfigString)

        configuration.reload()

        val reloadedConfig: TestConfigObject by configuration
        assertEquals(newConfig, reloadedConfig)
    }

    companion object {
        @TempDir
        private lateinit var tempDataDirectory: Path

        @TempDir
        private lateinit var defaultResourcesDirectory: Path
        private lateinit var registry: YamlConfigurationRegistry

        @BeforeAll
        @JvmStatic
        fun startup() {
            registry = YamlConfigurationRegistry {
                dataDirectory = tempDataDirectory
                defaultResource { yamlPath ->
                    defaultResourcesDirectory.resolve(yamlPath).takeIf { it.exists() }?.inputStream()
                }
                logger = LoggerFactory.getLogger(YamlConfigurationTest::class.java)
            }
        }
    }
}
