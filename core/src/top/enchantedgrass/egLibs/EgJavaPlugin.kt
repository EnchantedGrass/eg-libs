package top.enchantedgrass.egLibs

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import java.nio.file.Path

abstract class EgJavaPlugin : SuspendingJavaPlugin(), EgPlugin {
    override val dataDirectory: Path = dataFolder.toPath()

    @Suppress("UnstableApiUsage")
    override fun namespace(): String = pluginMeta.name.lowercase()

    override fun onLoad() {
        instance = this
        super.onLoad()
    }

    companion object {
        /**
         * The instance of the plugin using this library.
         * This is set when the plugin is loading.
         */
        lateinit var instance: EgJavaPlugin
    }
}
