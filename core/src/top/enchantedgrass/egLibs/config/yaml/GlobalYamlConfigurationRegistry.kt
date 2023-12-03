package top.enchantedgrass.egLibs.config.yaml

import top.enchantedgrass.egLibs.config.ConfigurationRegistry
import top.enchantedgrass.egLibs.config.yaml.GlobalYamlConfigurationRegistry.init

private val globalInstance = YamlConfigurationRegistry()
private var isInitialized = false

/**
 * A global [YamlConfigurationRegistry] instance.
 *
 * __Notes:__ Call [init] before using this instance.
 */
object GlobalYamlConfigurationRegistry : ConfigurationRegistry<YamlConfiguration> by globalInstance {
    /**
     * Initializes the global [YamlConfigurationRegistry] instance.
     * @throws IllegalStateException if the global instance is already initialized.
     */
    fun init(block: YamlConfigurationRegistry.Builder.() -> Unit) {
        check(!isInitialized) { "Registry is already initialized!" }
        globalInstance.builder.apply(block)
        isInitialized = true
    }
}
