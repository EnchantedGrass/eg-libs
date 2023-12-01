package top.enchantedgrass.egLibs.config.yaml

import net.kyori.adventure.key.Key
import top.enchantedgrass.egLibs.config.Configuration
import java.nio.file.Path
import kotlin.io.path.*
import kotlin.reflect.KClass
import kotlin.reflect.safeCast

private const val YAML_FILE_EXTENSION = ".yml"

class YamlConfiguration internal constructor(
    private val registry: YamlConfigurationRegistry,
    name: String,
    private val type: KClass<*>,
) : Configuration {
    private val yamlPathString by lazy { this.name + YAML_FILE_EXTENSION }
    private val defaultResource get() = registry.getDefaultResource(yamlPathString)
    private val configPath get() = registry.dataDirectory.resolve(yamlPathString)

    private var cache: Any? = null

    override val name: String = name.removeSuffix(YAML_FILE_EXTENSION)

    override fun <T : Any> unwrap(type: KClass<T>): T {
        checkNotNull(cache) { "Configuration $name is not registered yet." }
        require(type.isInstance(cache)) { "Configuration $name is not of type ${type.qualifiedName}." }
        return type.safeCast(cache) ?: error("Cannot cast ${cache!!::class.qualifiedName} to ${type.qualifiedName}.")
    }

    override fun reload() {
        onUnregister()
        onRegister()
    }

    override val key: Key = registry.keyFactory(name)

    override fun onRegister() {
        cache = registry.mapper.readValue(configPath.readYaml(), type.java)
    }

    override fun onUnregister() {
        cache = null
        registry.logger?.info("Unloaded configuration $name")
    }

    private fun Path.readYaml(): ByteArray {
        if (notExists()) {
            createParentDirectories()
            createFile()
            defaultResource?.let { defaultResource ->
                defaultResource.use { inputStream ->
                    outputStream().use { inputStream.transferTo(it) }
                }
            }
        }
        return readBytes()
    }
}
