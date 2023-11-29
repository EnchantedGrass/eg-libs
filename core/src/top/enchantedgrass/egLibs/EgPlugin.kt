package top.enchantedgrass.egLibs

import com.github.shynixn.mccoroutine.bukkit.SuspendingPlugin
import net.kyori.adventure.key.Key
import net.kyori.adventure.key.Namespaced
import java.nio.file.Path

interface EgPlugin : SuspendingPlugin, Namespaced {
    /**
     * The directory that the plugin data's files are located in.
     * The directory may not exist.
     */
    val dataDirectory: Path
}

/**
 * Creates new [Key] associated with [EgPlugin] plugin namespace and [value].
 */
fun EgPlugin.newKey(value: String) = Key.key(this, value)
