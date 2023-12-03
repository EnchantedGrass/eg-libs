package top.enchantedgrass.egLibs.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.bukkit.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails


class BukkitColorTest {
    private val mapper = ObjectMapper().registerModule(
        SimpleModule("bukkit-color").apply {
            addSerializer(Color::class.java, BukkitColor.Serializer)
            addDeserializer(Color::class.java, BukkitColor.Deserializer)
        }
    )

    @Test
    fun `Serializer should serialize color correctly`() {
        val color = Color.fromRGB(0xFF0000)
        val json = mapper.writeValueAsString(color)
        assertEquals("""{"alpha":255,"red":255,"green":0,"blue":0}""", json)
    }

    @Test
    fun `Deserializer should deserialize color object`() {
        val json = """{"alpha":9,"red":255,"green":0,"blue":0}"""
        val color = mapper.readValue<Color>(json)
        assertEquals(Color.fromARGB(0x09, 0xFF, 0, 0), color)
    }

    @Test
    fun `Deserializer should deserialize color object without alpha`() {
        val json = """{"red":255,"green":0,"blue":0}"""
        val color = mapper.readValue<Color>(json)
        assertEquals(Color.fromRGB(0xFF0000), color)
    }

    @Test
    fun `Deserializer should deserialize rgb color hex`() {
        val json = "\"#FF0000\""
        val color = mapper.readValue<Color>(json)
        assertEquals(Color.fromRGB(0xFF0000), color)
    }

    @Test
    fun `Deserializer should deserialize argb color hex`() {
        val json = "\"#FF0000FF\""
        val color = mapper.readValue<Color>(json)
        assertEquals(Color.fromARGB(0xFF, 0, 0, 0xFF), color)
    }

    @Test
    fun `Deserializer should throw exception when hex is invalid`() {
        val json = "\"#FF000\""
        assertFails("Invalid color format: FF000") {
            mapper.readValue<Color>(json)
        }
    }
}
