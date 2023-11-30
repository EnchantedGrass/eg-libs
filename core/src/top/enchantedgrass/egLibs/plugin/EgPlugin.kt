package top.enchantedgrass.egLibs.plugin

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

    /**
     * The plugin's namespace.
     * Use to create [Key]s associated with this plugin.
     */
    override fun namespace(): String
}
