package top.enchantedgrass.egLibs.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.NamespacedKey

object BukkitLocation {
    object Serializer : StdSerializer<Location>(Location::class.java) {
        private fun readResolve(): Any = Serializer

        override fun serialize(value: Location, gen: JsonGenerator, provider: SerializerProvider) = gen.run {
            writeStartObject()
            if (value.world != null) writeStringField("world", value.world.key.asString())
            writeNumberField("x", value.x)
            writeNumberField("y", value.y)
            writeNumberField("z", value.z)
            writeNumberField("yaw", value.yaw)
            writeNumberField("pitch", value.pitch)
            writeEndObject()
        }
    }

    object Deserializer : StdDeserializer<Location>(Location::class.java) {
        private fun readResolve(): Any = Deserializer

        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Location {
            val node = p.codec.readTree<ObjectNode>(p)
            val world = node["world"]?.asText()
            val x = node["x"]?.asDouble() ?: 0.0
            val y = node["y"]?.asDouble() ?: 0.0
            val z = node["z"]?.asDouble() ?: 0.0
            val yaw = node["yaw"]?.asDouble() ?: 0.0
            val pitch = node["pitch"]?.asDouble() ?: 0.0

            val bukkitWorld = world?.let {
                Bukkit.getWorld(it) ?: Bukkit.getWorld(
                    NamespacedKey.fromString(world) ?: NamespacedKey.minecraft(world)
                )
            }
            return Location(bukkitWorld, x, y, z, yaw.toFloat(), pitch.toFloat())
        }
    }
}
