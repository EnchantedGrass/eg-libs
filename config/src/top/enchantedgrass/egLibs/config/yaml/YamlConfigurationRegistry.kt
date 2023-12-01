package top.enchantedgrass.egLibs.config.yaml

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.slf4j.Logger
import top.enchantedgrass.egLibs.config.AbstractConfigurationRegistry
import java.io.InputStream
import java.nio.file.Path
import kotlin.reflect.KClass

/**
 * The container to load and store [YamlConfiguration]s.
 */
open class YamlConfigurationRegistry internal constructor(internal val builder: Builder) :
    AbstractConfigurationRegistry<YamlConfiguration>() {
    internal val mapper by lazy { builder.mapper }
    internal val dataDirectory by lazy { builder.dataDirectory }
    internal val getDefaultResource by lazy { builder.getDefaultResource }

    override val logger get() = builder.logger

    override fun <T : Any> create(id: String, type: KClass<T>) = YamlConfiguration(this, id, type)

    /**
     * The builder for [YamlConfigurationRegistry].
     */
    class Builder {
        /**
         * The [YAMLMapper] to use it for deserialization configurations.
         */
        var mapper: YAMLMapper = YAMLMapper(YAMLFactory())

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
            mapper.apply {
                setPropertyNamingStrategy(PropertyNamingStrategies.KEBAB_CASE)
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                registerKotlinModule()
            }
        }

        /**
         * Configures the [mapper] with the specified [block].
         * @return this [Builder] instance.
         */
        fun mapper(block: YAMLMapper.() -> Unit): Builder {
            mapper.apply(block)
            return this
        }

        /**
         * Sets the [fn] to get the default resource for the specified configuration name.
         * @return this [Builder] instance.
         */
        fun defaultResource(fn: (String) -> InputStream?): Builder {
            this.getDefaultResource = fn
            return this
        }
    }
}

/**
 * Creates a new [YamlConfigurationRegistry] with [builder].
 */
fun YamlConfigurationRegistry(builder: YamlConfigurationRegistry.Builder.() -> Unit = {}) =
    YamlConfigurationRegistry(YamlConfigurationRegistry.Builder().apply(builder))
