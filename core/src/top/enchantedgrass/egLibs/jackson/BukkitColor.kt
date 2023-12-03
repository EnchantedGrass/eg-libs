package top.enchantedgrass.egLibs.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.bukkit.Color

object BukkitColor {
    object Serializer : StdSerializer<Color>(Color::class.java) {
        private fun readResolve(): Any = Serializer

        override fun serialize(value: Color, gen: JsonGenerator, provider: SerializerProvider) = gen.run {
            writeStartObject()
            writeNumberField("alpha", value.alpha)
            writeNumberField("red", value.red)
            writeNumberField("green", value.green)
            writeNumberField("blue", value.blue)
            writeEndObject()
        }
    }

    object Deserializer : StdDeserializer<Color>(Color::class.java) {
        private const val HEX_RGB_LENGTH = 6
        private const val HEX_ARGB_LENGTH = 8

        private fun readResolve(): Any = Deserializer

        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Color {
            val node = p.codec.readTree<JsonNode>(p)
            return when {
                node.isObject -> {
                    val alpha = node["alpha"]?.asInt() ?: 255
                    val red = node["red"].asInt()
                    val green = node["green"].asInt()
                    val blue = node["blue"].asInt()

                    Color.fromARGB(alpha, red, green, blue)
                }

                else -> {
                    val hex = node.asText().removePrefix("#")
                    require(hex.length == HEX_RGB_LENGTH || hex.length == HEX_ARGB_LENGTH) {
                        "Invalid color format: $hex"
                    }
                    if (hex.length == HEX_RGB_LENGTH)
                        Color.fromRGB(hex.toInt(16))
                    else
                        Color.fromARGB(hex.toLong(16).toInt())
                }
            }
        }
    }
}
