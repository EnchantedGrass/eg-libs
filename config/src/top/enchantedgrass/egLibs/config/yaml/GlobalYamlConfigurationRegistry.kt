package top.enchantedgrass.egLibs.config.yaml

import org.slf4j.Logger
import top.enchantedgrass.egLibs.config.ConfigurationRegistry
import kotlin.reflect.KClass


object GlobalYamlConfigurationRegistry : ConfigurationRegistry<YamlConfiguration>() {
    private val
    override var logger: Logger?
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun <T : Any> create(name: String, type: KClass<T>): YamlConfiguration {
        TODO("Not yet implemented")
    }

}
