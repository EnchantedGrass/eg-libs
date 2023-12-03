package top.enchantedgrass.egLibs.jackson

import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.bukkit.Color
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails


class BukkitColorTest {
    @Test
    fun `Serializer should serialize color correctly`() {
        val color = Color.fromRGB(0xFF0000)
        val serialized = JacksonYaml.mapper.writeValueAsString(color).trimIndent()
        val expected = """
            alpha: 255
            red: 255
            green: 0
            blue: 0
            """.trimIndent()
        assertEquals(expected, serialized)
    }

    @Test
    fun `Deserializer should deserialize color object`() {
        val serialized = """
            alpha: 9
            red: 255
            green: 0
            blue: 0
            """.trimIndent()
        val deserialized = JacksonYaml.mapper.readValue(serialized, Color::class.java)
        assertEquals(Color.fromARGB(0x09FF0000), deserialized)
    }

    @Test
    fun `Deserializer should deserialize color object without alpha`() {
        val serialized = """
            red: 255
            green: 0
            blue: 0
            """.trimIndent()
        val deserialized = JacksonYaml.mapper.readValue<Color>(serialized)
        assertEquals(Color.fromRGB(0xFF0000), deserialized)
    }

    @Test
    fun `Deserializer should deserialize rgb color hex`() {
        val serialized = "'#FF0000'"
        val deserialized = JacksonYaml.mapper.readValue<Color>(serialized)
        assertEquals(Color.fromRGB(0xFF0000), deserialized)
    }

    @Test
    fun `Deserializer should deserialize argb color hex`() {
        val serialized = "'#FF0000FF'"
        val deserialized = JacksonYaml.mapper.readValue<Color>(serialized)
        assertEquals(Color.fromARGB(0xFF, 0, 0, 0xFF), deserialized)
    }

    @Test
    fun `Deserializer should throw exception when hex is invalid`() {
        val serialized = "'#FF000'"
        assertFails("Invalid color format: FF000") {
            JacksonYaml.mapper.readValue<Color>(serialized)
        }
    }

    companion object {
        @JvmStatic
        @BeforeAll
        fun setUpAll() {
            JacksonYaml.configure {
                SimpleModule("bukkit-color").apply {
                    addSerializer(Color::class.java, BukkitColor.Serializer)
                    addDeserializer(Color::class.java, BukkitColor.Deserializer)
                }.also { registerModule(it) }
            }
        }
    }
}
