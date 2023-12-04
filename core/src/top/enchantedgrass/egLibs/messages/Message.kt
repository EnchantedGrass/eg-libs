package top.enchantedgrass.egLibs.messages

import net.kyori.adventure.audience.Audience
import net.kyori.adventure.sound.Sound
import net.kyori.adventure.sound.Sound.Source
import net.kyori.adventure.title.Title
import org.bukkit.NamespacedKey
import top.enchantedgrass.egLibs.component.Placeholder
import top.enchantedgrass.egLibs.component.mdc
import top.enchantedgrass.egLibs.extensions.orEmpty
import kotlin.time.Duration
import kotlin.time.toJavaDuration

/**
 * Represents a message that can be sent to a [net.kyori.adventure.audience.Audience].
 *
 * @see TextMessage
 * @see ActionBarMessage
 * @see TitleMessage
 * @see SoundMessage
 * @see CompositeMessage
 */
sealed interface Message

/**
 * Represents a text message that can be sent to an audience.
 *
 * @property text The text content of the message.
 *
 * @see net.kyori.adventure.audience.Audience.sendMessage
 */
data class TextMessage(val text: String) : Message

/**
 * Represents an action bar message that can be sent to an audience.
 *
 * @property actionBar The action bar content of the message.
 *
 * @see net.kyori.adventure.audience.Audience.sendActionBar
 */
data class ActionBarMessage(val actionBar: String) : Message

/**
 * Represents a title message that can be displayed on audience screen.
 *
 * @property title The title of the message.
 * @property subtitle The subtitle of the message.
 * @property times The times for the message to fadeIn, stay, and fadeOut.
 *
 * @see net.kyori.adventure.audience.Audience.showTitle
 */
data class TitleMessage(val title: String? = null, val subtitle: String? = null, val times: Times? = null) : Message {
    /**
     * Represents the duration for the fadeIn, stay, and fadeOut effects of a title message.
     *
     * @property fadeIn The duration of the fadeIn effect in milliseconds.
     * @property stay The duration the message should stay on screen in milliseconds.
     * @property fadeOut The duration of the fadeOut effect in milliseconds.
     */
    data class Times(val fadeIn: Duration, val stay: Duration, val fadeOut: Duration)
}

/**
 * Represents a sound that can be played to an audience.
 *
 * @property sound The sound to play.
 * @property source The source of the sound.
 * @property volume The volume of the sound. Must be a positive number.
 * @property pitch The pitch of the sound. Must be between -1 and 1.
 *
 * @see org.bukkit.Sound
 * @see net.kyori.adventure.audience.Audience.playSound
 */
data class SoundMessage(
    val sound: String,
    val source: Source? = null,
    val volume: Float? = null,
    val pitch: Float? = null,
) : Message

/**
 * Represents a composite message that can be sent to an audience.
 *
 * @property messages The list of messages in the composite message.
 */
data class CompositeMessage(val messages: List<Message> = emptyList()) : Message

/**
 * Sends a message to the audience.
 *
 * This function use [de.themoep.minedown.adventure.MineDown] to deserialize the message to a component.
 *
 * @param message The message to be sent.
 * @param placeholders The placeholders to be replaced in the content message.
 */
fun Audience.sendMessage(message: Message, vararg placeholders: Placeholder) =
    sendMessage(message, placeholders.toList())

/**
 * Sends a message to the audience.
 *
 * This function use [de.themoep.minedown.adventure.MineDown] to deserialize the message to a component.
 *
 * @param message The message to be sent.
 * @param placeholders The placeholders to be replaced in the content message.
 */
fun Audience.sendMessage(message: Message, placeholders: List<Placeholder> = emptyList()) {
    when (message) {
        is TextMessage -> sendMessage(message.text.mdc(placeholders))
        is ActionBarMessage -> sendActionBar(message.actionBar.mdc(placeholders))
        is TitleMessage -> showTitle(message.toAdventureTitle(placeholders))
        is SoundMessage -> playSound(message.toAdventureSound())
        is CompositeMessage -> message.messages.forEach { sendMessage(it, placeholders) }
    }
}

private fun TitleMessage.toAdventureTitle(placeholders: List<Placeholder>) = Title.title(
    title?.mdc(placeholders).orEmpty,
    subtitle?.mdc(placeholders).orEmpty,
    times?.toAdventureTimes(),
)

private fun TitleMessage.Times.toAdventureTimes() = Title.Times.times(
    fadeIn.toJavaDuration(),
    stay.toJavaDuration(),
    fadeOut.toJavaDuration(),
)

private fun SoundMessage.toAdventureSound() = Sound.sound().run {
    val type = try {
        org.bukkit.Sound.valueOf(sound.uppercase()).key
    } catch (e: IllegalArgumentException) {
        val key = NamespacedKey.minecraft(sound)
        org.bukkit.Sound.entries.firstOrNull { it.key == key }?.key ?: key
    }
    type(type)
    source?.let { source(it) }
    volume?.let { volume(volume) }
    pitch?.let { pitch(pitch) }
    build()
}
