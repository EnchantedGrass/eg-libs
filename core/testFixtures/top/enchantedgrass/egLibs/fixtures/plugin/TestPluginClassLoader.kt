package top.enchantedgrass.egLibs.fixtures.plugin

import io.papermc.paper.plugin.provider.classloader.ConfiguredPluginClassLoader
import org.bukkit.plugin.java.JavaPlugin

@Suppress("UnstableApiUsage")
class TestPluginClassLoader : ClassLoader(), ConfiguredPluginClassLoader {
    private var holder: JavaPlugin? = null

    override fun close() = Unit

    override fun getConfiguration() = holder?.pluginMeta

    override fun loadClass(name: String, resolve: Boolean, checkGlobal: Boolean, checkLibraries: Boolean): Class<*> =
        super.loadClass(name, resolve)

    override fun init(plugin: JavaPlugin) {
        holder = plugin
    }

    override fun getPlugin() = holder

    override fun getGroup() = throw NotImplementedError()
}
