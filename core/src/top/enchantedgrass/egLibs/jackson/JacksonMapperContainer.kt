package top.enchantedgrass.egLibs.jackson

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * The JacksonMapperContainer interface represents a container for the Jackson ObjectMapper instance.
 *
 * @see JacksonYaml
 */
interface JacksonMapperContainer {
    /**
     * The mapper variable is an instance of the ObjectMapper class from the Jackson library.
     * It is used for the serialization and deserialization of JSON objects.
     **/
    val mapper: ObjectMapper

    /**
     * Configures the [mapper] with the provided [action].
     */
    fun configure(action: ObjectMapper.() -> Unit) = action(mapper)
}
