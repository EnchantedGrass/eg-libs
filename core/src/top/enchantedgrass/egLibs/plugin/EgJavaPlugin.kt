package top.enchantedgrass.egLibs.plugin

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import java.nio.file.Path

abstract class EgJavaPlugin : SuspendingJavaPlugin(), EgPlugin {
    override val dataDirectory: Path = dataFolder.toPath()

    @Suppress("UnstableApiUsage")
    override fun namespace(): String = pluginMeta.name.lowercase()
}
