package top.enchantedgrass.egLibs.items

import com.destroystokyo.paper.profile.ProfileProperty
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import top.enchantedgrass.egLibs.component.Placeholder
import top.enchantedgrass.egLibs.component.mdc
import top.enchantedgrass.egLibs.component.mdcList
import top.enchantedgrass.egLibs.nbt.NBTItem
import java.util.*

private const val MINECRAFT_USERNAME_MAX_LENGTH = 16
private const val TEXTURES_PROPERTY_KEY = "textures"

/**
 * Converts a FastItemStack to a Bukkit ItemStack.
 * @constructor Create a FastItemStackConverter with the provided FastItemStack.
 * @param fastStack The FastItemStack to convert.
 */
class FastItemStackConverter(private val fastStack: FastItemStack) {
    /**
     * Converts the FastItemStack to a Bukkit ItemStack.
     *
     * @param placeholders The list of placeholders to be replaced in the item properties.
     * @return The converted Bukkit ItemStack.
     */
    fun asBukkit(placeholders: List<Placeholder>): ItemStack {
        val itemStack = ItemStack(prepareMaterial(), prepareAmount())
        itemStack.editMeta { meta ->
            meta.displayName(prepareDisplayNameComponent(placeholders))
            meta.lore(prepareLoreComponent(placeholders))
            determineProp(FastItemStack::customModelData)?.let { meta.setCustomModelData(it) }
            meta.applyEnchants()
            setPropIfPresent(FastItemStack::flags) { meta.addItemFlags(*it.toTypedArray()) }
            setPropIfPresent(FastItemStack::isUnbreakable) { meta.isUnbreakable = it }
            meta.applyGlowing()
            meta.applyIfIsInstance<Damageable> {
                setPropIfPresent(FastItemStack::damage) { damage = it }
            }
            meta.applySkullTextureIfPresent()
            meta.applyIfIsInstance<LeatherArmorMeta> {
                setPropIfPresent(FastItemStack::leatherArmorColor) { setColor(it) }
            }
            meta.applyIfIsInstance<BannerMeta> {
                setPropIfPresent(FastItemStack::bannerPatterns) { patterns = it }
            }
            meta.applyIfIsInstance<ArmorMeta> {
                setPropIfPresent(FastItemStack::armorTrim) { trim = it }
            }
            meta.applyIfIsInstance<PotionMeta> {
                setPropIfPresent(FastItemStack::potionColor) { color = it }
            }
        }
        itemStack.applyNbtTags()
        return itemStack
    }

    private fun prepareMaterial() = determineProp(FastItemStack::material)!!

    private fun prepareAmount() = determineProp(FastItemStack::amount) ?: 1

    private fun prepareDisplayNameComponent(placeholders: List<Placeholder>): Component? {
        val component = determineProp(FastItemStack::displayName)
            ?.mdc(placeholders + listOf("current" to (fastStack.displayName ?: "")))
        return component?.decoration(TextDecoration.ITALIC, false)
    }

    private fun prepareLoreComponent(placeholders: List<Placeholder>): List<Component>? {
        val loreComponents = determineProp(FastItemStack::lore)
            ?.trimEnd('\n', ' ')
            ?.mdcList(placeholders + listOf("current" to (fastStack.lore ?: "")))
        return loreComponents?.map { it.decoration(TextDecoration.ITALIC, false) }
    }

    private fun ItemMeta.applyEnchants() {
        setPropIfPresent(FastItemStack::enchants) { enchants ->
            enchants.forEach { (name, value) ->
                val enchantment = requireNotNull(Enchantment.getByKey(NamespacedKey.minecraft(name))) {
                    "Unknown enchantment: $name"
                }
                addEnchant(enchantment, value, true)
            }
        }
    }

    private fun ItemMeta.applyGlowing() {
        if (determineProp(FastItemStack::isGlowing) == true) {
            addEnchant(Enchantment.DURABILITY, 1, true)
            addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }

    private fun ItemMeta.applySkullTextureIfPresent() {
        setPropIfPresent(FastItemStack::skullTexture) {
            applyIfIsInstance<SkullMeta> {
                if (it.length <= MINECRAFT_USERNAME_MAX_LENGTH) return@setPropIfPresent applyPlayerHead(it)
                playerProfile = Bukkit.createProfile(UUID.randomUUID()).apply {
                    setProperty(ProfileProperty(TEXTURES_PROPERTY_KEY, it))
                }
            }
        }
    }

    private fun SkullMeta.applyPlayerHead(playerName: String) {
        owningPlayer = Bukkit.getOfflinePlayer(playerName)
    }

    private fun ItemStack.applyNbtTags() {
        setPropIfPresent(FastItemStack::nbtTags) { nbtTags ->
            if (nbtTags.isEmpty()) return@setPropIfPresent
            NBTItem(this).run {
                for ((key, value) in nbtTags) {
                    this[key] = value
                }
                applyNBT(this@applyNbtTags)
            }
        }
    }

    private operator fun NBTItem.set(key: String, value: Any) {
        when (value) {
            is String -> setString(key, value)
            is Byte -> setByte(key, value)
            is ByteArray -> setByteArray(key, value)
            is Short -> setShort(key, value)
            is Int -> setInteger(key, value)
            is IntArray -> setIntArray(key, value)
            is Long -> setLong(key, value)
            is LongArray -> setLongArray(key, value)
            is Float -> setFloat(key, value)
            is Double -> setDouble(key, value)
            is Boolean -> setBoolean(key, value)
            else -> throw IllegalArgumentException("Unsupported NBT type: ${value::class.simpleName}")
        }
    }

    private fun <T> setPropIfPresent(getter: (FastItemStack) -> T?, setter: (T) -> Unit) {
        determineProp(getter)?.let(setter)
    }

    private fun <T> determineProp(getter: (FastItemStack) -> T?): T? =
        fastStack.merge?.let(getter) ?: fastStack.let(getter)

    private inline fun <reified M : ItemMeta> ItemMeta.applyIfIsInstance(block: M.() -> Unit) {
        if (this is M) block()
    }
}
