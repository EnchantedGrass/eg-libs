@file:Suppress("unused")

package top.enchantedgrass.egLibs.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.bukkit.NamespacedKey
import org.bukkit.Registry
import org.bukkit.inventory.meta.trim.ArmorTrim

object BukkitArmorTrim {
    object Serializer : StdSerializer<ArmorTrim>(ArmorTrim::class.java) {
        private fun readResolve(): Any = Serializer

        override fun serialize(value: ArmorTrim, gen: JsonGenerator, provider: SerializerProvider) = gen.run {
            writeStartObject()
            writeStringField("material", value.material.key.asString())
            writeStringField("pattern", value.pattern.key.asString())
            writeEndObject()
        }
    }

    object Deserializer : StdDeserializer<ArmorTrim>(ArmorTrim::class.java) {
        private fun readResolve(): Any = Deserializer

        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ArmorTrim {
            val node = p.codec.readTree<ObjectNode>(p)
            val materialName = node["material"].asText()
            val patternName = node["pattern"].asText()

            val materialKey = NamespacedKey.fromString(materialName) ?: NamespacedKey.minecraft(materialName)
            val patternKey = NamespacedKey.fromString(patternName) ?: NamespacedKey.minecraft(patternName)

            val material = requireNotNull(Registry.TRIM_MATERIAL[materialKey]) {
                "Invalid trim material: $materialName"
            }
            val pattern = requireNotNull(Registry.TRIM_PATTERN[patternKey]) {
                "Invalid trim pattern: $patternName"
            }

            return ArmorTrim(material, pattern)
        }
    }
}
