package top.enchantedgrass.egLibs.fixtures.plugin

import io.mockk.mockkClass
import top.enchantedgrass.egLibs.plugin.EgJavaPlugin
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
fun mockkEgJavaPlugin(
    name: String? = null,
    relaxed: Boolean = false,
    vararg moreInterfaces: KClass<*>,
    relaxUnitFun: Boolean = false,
    block: EgJavaPlugin.() -> Unit = {},
): EgJavaPlugin {
    val classLoader = TestPluginClassLoader()
    val pluginClass = classLoader.loadClass("top.enchantedgrass.egLibs.plugin.EgJavaPlugin") as Class<EgJavaPlugin>
    return mockkClass(pluginClass.kotlin, name, relaxed, *moreInterfaces, relaxUnitFun = relaxUnitFun, block = block)
}
