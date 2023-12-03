package top.enchantedgrass.egLibs.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.bukkit.Location
import kotlin.test.Test
import kotlin.test.assertEquals

class BukkitLocationTest {
    private val mapper = ObjectMapper().registerKotlinModule().registerModule(
        SimpleModule("bukkit-location").apply {
            addSerializer(Location::class.java, BukkitLocation.Serializer)
            addDeserializer(Location::class.java, BukkitLocation.Deserializer)
        }
    )

    @Test
    fun `Serializer should serialize Location`() {
        val location = Location(null, 1.0, 2.0, 3.0, 4.0f, 5.0f)
        val json = mapper.writeValueAsString(location)
        assertEquals("""{"x":1.0,"y":2.0,"z":3.0,"yaw":4.0,"pitch":5.0}""", json)
    }

    @Test
    fun `Deserializer should deserialize Location`() {
        val json = """{"x":1.0,"y":2.0,"z":3.0,"yaw":4.0,"pitch":5.0}"""
        val location = mapper.readValue(json, Location::class.java)
        assertEquals(1.0, location.x)
        assertEquals(2.0, location.y)
        assertEquals(3.0, location.z)
        assertEquals(4.0f, location.yaw)
        assertEquals(5.0f, location.pitch)
    }
}
