package top.enchantedgrass.egLibs.jackson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

/**
 * Provides a Jackson `ObjectMapper` instance specifically configured for YAML serialization and deserialization.
 */
object JacksonYaml : JacksonMapperContainer {
    /**
     * YAML mapper that is used for mapping YAML data to Kotlin objects and vice versa.
     */
    override val mapper: YAMLMapper = YAMLMapper.builder().enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).build()

    init {
        mapper.configureDefaults()
    }

    private fun YAMLMapper.configureDefaults() {
        propertyNamingStrategy = PropertyNamingStrategies.KEBAB_CASE
        registerKotlinModule()
        disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
    }
}
