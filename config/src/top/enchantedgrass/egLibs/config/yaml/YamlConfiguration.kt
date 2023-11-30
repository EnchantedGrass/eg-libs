package top.enchantedgrass.egLibs.config.yaml

import net.kyori.adventure.key.Key
import ru.vyarus.yaml.updater.YamlUpdater
import top.enchantedgrass.egLibs.config.Configuration
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.notExists
import kotlin.io.path.readBytes
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

private const val YAML_FILE_EXTENSION = ".yml"

class YamlConfiguration internal constructor(
    name: String,
    private val type: KClass<*>,
    override val key: Key,
) : Configuration {
    private val yamlPathString by lazy { this.name + YAML_FILE_EXTENSION }
    private val defaultResource get() = YamlConfigurationRegistry.getResource(yamlPathString)
    private val configPath get() = YamlConfigurationRegistry.dataDirectory.resolve(yamlPathString)

    private var cache: Any? = null

    override val name: String = name.removeSuffix(YAML_FILE_EXTENSION)

    override fun <T : Any> unwrap(type: KClass<T>): T {
        checkNotNull(cache) { "Configuration $name is not registered yet." }
        check(type.isInstance(cache)) { "Configuration $name is not of type ${type.qualifiedName}." }
        return type.safeCast(cache) ?: error("Cannot cast ${cache!!::class.qualifiedName} to ${type.qualifiedName}.")
    }

    override fun reload() {
        onUnregister()
        onRegister()
    }

    override fun onRegister() {
        cache = YamlConfigurationRegistry.mapper.readValue(configPath.readYaml(), type.java)
    }

    override fun onUnregister() {
        cache = null
        YamlConfigurationRegistry.logger?.info("Unloaded configuration $name")
    }

    private fun Path.readYaml(): ByteArray {
        if (notExists()) createFile()
        defaultResource?.let {
            YamlUpdater.create(configPath.toFile(), it).backup(true).update()
        }
        return readBytes()
    }
}
