package top.enchantedgrass.egLibs.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import net.kyori.adventure.sound.Sound.Source
import top.enchantedgrass.egLibs.messages.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.seconds

class EgMessageTest {
    private val mapper = ObjectMapper().registerKotlinModule().registerModule(
        SimpleModule("eg-message")
            .addSerializer(Message::class.java, EgMessage.Serializer)
            .addDeserializer(Message::class.java, EgMessage.Deserializer)
    )

    @Test
    fun `Serializer should serialize TextMessage correctly`() {
        val message = TextMessage("Hello, world!")
        val json = mapper.writeValueAsString(message)
        assertEquals("\"Hello, world!\"", json)
    }

    @Test
    fun `Serializer should serialize ActionBarMessage correctly`() {
        val message = ActionBarMessage("Hello, world!")
        val json = mapper.writeValueAsString(message)
        assertEquals("{\"action-bar\":\"Hello, world!\"}", json)
    }

    @Test
    fun `Serializer should serialize TitleMessage correctly`() {
        val message = TitleMessage("Hello, world!", "Hello, world!")
        val json = mapper.writeValueAsString(message)
        assertEquals("{\"title\":\"Hello, world!\",\"subtitle\":\"Hello, world!\"}", json)
    }

    @Test
    fun `Serializer should serialize SoundMessage correctly`() {
        val message = SoundMessage("entity.player.levelup")
        val json = mapper.writeValueAsString(message)
        assertEquals("{\"sound\":\"entity.player.levelup\"}", json)
    }

    @Test
    fun `Serializer should serialize CompositeMessage correctly`() {
        val message = CompositeMessage(
            listOf(
                TextMessage("Hello, world!"),
                ActionBarMessage("Hello, world!"),
                TitleMessage("Hello, world!", "Hello, world!"),
                SoundMessage("entity.player.levelup")
            )
        )
        val json = mapper.writeValueAsString(message)
        assertEquals(
            "[\"Hello, world!\",{\"action-bar\":\"Hello, world!\"},{\"title\":\"Hello, world!\",\"subtitle\":\"Hello, world!\"},{\"sound\":\"entity.player.levelup\"}]",
            json
        )
    }

    @Test
    fun `Deserializer should deserialize TextMessage correctly`() {
        val content = "Hello, world!"
        val json = "{\"text\":\"$content\"}"
        val message = mapper.readValue<Message>(json)
        assertIs<TextMessage>(message)
        assertEquals(content, message.text)
    }

    @Test
    fun `Deserializer should deserialize TextMessage without text field`() {
        val content = "Hello, world!"
        val json = "\"$content\""
        val message = mapper.readValue<Message>(json)
        assertIs<TextMessage>(message)
        assertEquals(content, message.text)
    }

    @Test
    fun `Deserializer should deserialize ActionBarMessage correctly`() {
        val content = "Hello, world!"
        val json = "{\"action-bar\":\"$content\"}"
        val message = mapper.readValue<Message>(json)
        assertIs<ActionBarMessage>(message)
        assertEquals(content, message.actionBar)
    }

    @Test
    fun `Deserializer should deserialize TitleMessage correctly`() {
        val title = "Hello, world!"
        val subtitle = "Hello, world!"
        val json = "{\"title\":\"$title\",\"subtitle\":\"$subtitle\"}"
        val message = mapper.readValue<Message>(json)
        assertIs<TitleMessage>(message)
        assertEquals(title, message.title)
        assertEquals(subtitle, message.subtitle)
    }

    @Test
    fun `Deserializer should deserializer TitleMessage without title`() {
        val subtitle = "Hello, world!"
        val json = "{\"subtitle\":\"$subtitle\"}"
        val message = mapper.readValue<Message>(json)
        assertIs<TitleMessage>(message)
        assertNull(message.title)
        assertEquals(subtitle, message.subtitle)
    }

    @Test
    fun `Deserializer should deserializer TitleMessage with times`() {
        val fadeIn = 2.seconds
        val stay = 3.seconds
        val fadeOut = 4.seconds
        val json =
            "{\"title\":\"Hello, world!\",\"subtitle\":\"Hello, world!\",\"times\":{\"fade-in\":\"$fadeIn\",\"stay\":\"$stay\",\"fade-out\":\"$fadeOut\"}}"
        val message = mapper.readValue<Message>(json)
        assertIs<TitleMessage>(message)
        assertEquals(fadeIn, message.times?.fadeIn)
        assertEquals(stay, message.times?.stay)
        assertEquals(fadeOut, message.times?.fadeOut)
    }

    @Test
    fun `Deserializer should deserialize SoundMessage correctly`() {
        val sound = "entity.player.levelup"
        val json = "{\"sound\":\"$sound\"}"
        val message = mapper.readValue<Message>(json)
        assertIs<SoundMessage>(message)
        assertEquals(sound, message.sound)
    }

    @Test
    fun `Deserializer should deserialize SoundMessage with all props`() {
        val sound = "entity.player.levelup"
        val source = Source.MASTER
        val volume = 1.0f
        val pitch = 1.0f
        val json = "{\"sound\":\"$sound\",\"source\":\"$source\",\"volume\":$volume,\"pitch\":$pitch}"
        val message = mapper.readValue<Message>(json)
        assertIs<SoundMessage>(message)
        assertEquals(sound, message.sound)
        assertEquals(source, message.source)
        assertEquals(volume, message.volume)
        assertEquals(pitch, message.pitch)
    }

    @Test
    fun `Deserializer should deserialize CompositeMessage correctly`() {
        val json =
            "[\"Hello, world!\",{\"action-bar\":\"Hello, world!\"},{\"title\":\"Hello, world!\",\"subtitle\":\"Hello, world!\"},{\"sound\":\"entity.player.levelup\"}]"
        val message = mapper.readValue<Message>(json)
        assertIs<CompositeMessage>(message)
        assertEquals(4, message.messages.size)
        assertIs<TextMessage>(message.messages[0])
        assertIs<ActionBarMessage>(message.messages[1])
        assertIs<TitleMessage>(message.messages[2])
        assertIs<SoundMessage>(message.messages[3])
    }
}
