package top.enchantedgrass.egLibs.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import net.kyori.adventure.sound.Sound.Source
import top.enchantedgrass.egLibs.messages.*
import kotlin.time.Duration

object EgMessage {
    private const val TEXT_FIELD = "text"
    private const val ACTION_BAR_FIELD = "action-bar"
    private const val SOUND_FIELD = "sound"
    private const val SOURCE_FIELD = "source"
    private const val VOLUME_FIELD = "volume"
    private const val PITCH_FIELD = "pitch"
    private const val TITLE_FIELD = "title"
    private const val SUBTITLE_FIELD = "subtitle"
    private const val TIMES_FIELD = "times"
    private const val FADE_IN_FIELD = "fade-in"
    private const val STAY_FIELD = "stay"
    private const val FADE_OUT_FIELD = "fade-out"

    object Serializer : StdSerializer<Message>(Message::class.java) {
        private fun readResolve(): Any = Serializer

        override fun serialize(value: Message, gen: JsonGenerator, provider: SerializerProvider): Unit = gen.run {
            when (value) {
                is TextMessage -> writeString(value.text)
                is ActionBarMessage -> {
                    writeStartObject()
                    writeStringField(ACTION_BAR_FIELD, value.actionBar)
                    writeEndObject()
                }

                is TitleMessage -> {
                    writeStartObject()
                    writeStringField(TITLE_FIELD, value.title)
                    writeStringField(SUBTITLE_FIELD, value.subtitle)
                    value.times?.let {
                        writeObjectFieldStart(TIMES_FIELD)
                        writeStringField(FADE_IN_FIELD, it.fadeIn.toString())
                        writeStringField(STAY_FIELD, it.stay.toString())
                        writeStringField(FADE_OUT_FIELD, it.fadeOut.toString())
                        writeEndObject()
                    }
                    writeEndObject()
                }

                is SoundMessage -> {
                    writeStartObject()
                    writeStringField(SOUND_FIELD, value.sound)
                    value.source?.let { writeStringField(SOURCE_FIELD, it.name) }
                    value.volume?.let { writeNumberField(VOLUME_FIELD, it) }
                    value.pitch?.let { writeNumberField(PITCH_FIELD, it) }
                    writeEndObject()
                }

                is CompositeMessage -> {
                    writeStartArray()
                    value.messages.forEach { this@Serializer.serialize(it, gen, provider) }
                    writeEndArray()
                }
            }
        }
    }

    object Deserializer : StdDeserializer<Message>(Message::class.java) {
        private fun readResolve(): Any = Deserializer

        override fun deserialize(p: JsonParser, ctxt: DeserializationContext) =
            p.codec.readTree<JsonNode>(p).deserialize()

        private fun JsonNode.deserialize(): Message = when {
            isTextual -> TextMessage(textValue())
            isArray -> CompositeMessage(map { it.deserialize() })
            hasNonNull(TEXT_FIELD) -> TextMessage(this[TEXT_FIELD].textValue())
            hasNonNull(ACTION_BAR_FIELD) -> ActionBarMessage(this[ACTION_BAR_FIELD].textValue())
            hasNonNull(SOUND_FIELD) -> {
                val sound = this[SOUND_FIELD].textValue()
                val source = this[SOURCE_FIELD]?.let { Source.valueOf(it.textValue().uppercase()) }
                val volume = this[VOLUME_FIELD]?.floatValue()
                val pitch = this[PITCH_FIELD]?.floatValue()
                SoundMessage(sound, source, volume, pitch)
            }

            has(TITLE_FIELD) || has(SUBTITLE_FIELD) -> {
                val title = this[TITLE_FIELD]?.textValue()
                val subtitle = this[SUBTITLE_FIELD]?.textValue()
                val times = this[TIMES_FIELD]?.let {
                    TitleMessage.Times(
                        it[FADE_IN_FIELD].textValue().let(Duration::parse),
                        it[STAY_FIELD].textValue().let(Duration::parse),
                        it[FADE_OUT_FIELD].textValue().let(Duration::parse),
                    )
                }
                TitleMessage(title, subtitle, times)
            }

            else -> TextMessage(toString())
        }
    }
}
