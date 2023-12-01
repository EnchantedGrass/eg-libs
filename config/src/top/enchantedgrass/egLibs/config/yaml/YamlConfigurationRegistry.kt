package top.enchantedgrass.egLibs.config.yaml

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import net.kyori.adventure.key.Key
import org.slf4j.Logger
import top.enchantedgrass.egLibs.config.ConfigurationRegistry
import java.io.InputStream
import java.nio.file.Path
import kotlin.reflect.KClass

/**
 * The container to load and store [YamlConfiguration]s.
 */
class YamlConfigurationRegistry internal constructor(builder: Builder) : ConfigurationRegistry<YamlConfiguration>() {
    internal val mapper = builder.mapper
    internal val dataDirectory = builder.dataDirectory
    internal val getDefaultResource = builder.getDefaultResource
    private val keyFactory = builder.keyFactory

    override var logger: Logger? = builder.logger

    override fun <T : Any> create(name: String, type: KClass<T>) = YamlConfiguration(this, name, type, keyFactory(name))

    /**
     * The builder for [YamlConfigurationRegistry].
     */
    class Builder {
        /**
         * The [YAMLMapper] to use it for deserialization configurations.
         */
        var mapper: YAMLMapper = YAMLMapper(YAMLFactory())

        /**
         * The [Key] factory to create a [Key] for [YamlConfiguration].
         */
        var keyFactory: (String) -> Key = { Key.key(it) }

        /**
         * The directory to store configurations.
         */
        var dataDirectory: Path = Path.of("")

        /**
         * The function to get the default resource for the specified configuration name.
         */
        var getDefaultResource: (String) -> InputStream? = { null }

        /**
         * The logger to log the loading and unloading of configurations.
         */
        var logger: Logger? = null

        init {
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES).registerKotlinModule()
        }
    }
}

/**
 * Creates a new [YamlConfigurationRegistry] with [builder].
 */
fun YamlConfigurationRegistry(builder: YamlConfigurationRegistry.Builder.() -> Unit) =
    YamlConfigurationRegistry(YamlConfigurationRegistry.Builder().apply(builder))
