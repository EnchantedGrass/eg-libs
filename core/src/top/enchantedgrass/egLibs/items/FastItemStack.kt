package top.enchantedgrass.egLibs.items

import com.destroystokyo.paper.profile.ProfileProperty
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.block.banner.Pattern
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.*
import org.bukkit.inventory.meta.trim.ArmorTrim
import top.enchantedgrass.egLibs.component.Placeholder
import top.enchantedgrass.egLibs.component.mdc
import top.enchantedgrass.egLibs.component.mdcList
import top.enchantedgrass.egLibs.nbt.NBTItem
import java.util.*


/**
 * Represents a fast and customizable ItemStack.
 *
 * @property material The material of the ItemStack.
 * @property amount The amount of the ItemStack.
 * @property displayName The display name of the ItemStack.
 * @property lore The lore of the ItemStack.
 * @property customModelData The custom model data of the ItemStack.
 * @property enchants The enchantments of the ItemStack.
 * @property flags The flags of the ItemStack.
 * @property isUnbreakable Whether the ItemStack is unbreakable.
 * @property isGlowing Whether the ItemStack is glowing.
 * @property nbtTags The NBT tags of the ItemStack.
 * @property damage The durability of the ItemStack.
 * @property skullTexture The skull texture of the ItemStack if it is a player head.
 * @property leatherArmorColor The leather armor color of the ItemStack if it is a leather armor.
 * @property bannerPatterns The banner patterns of the ItemStack if it is a banner.
 * @property armorTrim The armor trim of the ItemStack if it is an armor.
 * @property potionColor The potion color of the ItemStack if it is a potion.
 * @property merge The ItemStack to merge with this ItemStack.
 */
data class FastItemStack(
    var material: Material? = null,
    var amount: Int? = null,
    var displayName: String? = null,
    var lore: String? = null,
    var customModelData: Int? = null,
    val enchants: MutableMap<String, Int> = mutableMapOf(),
    val flags: MutableSet<ItemFlag> = mutableSetOf(),
    var isUnbreakable: Boolean? = null,
    var isGlowing: Boolean? = null,
    val nbtTags: MutableMap<String, Any> = mutableMapOf(),
    val damage: Int? = null,
    val skullTexture: String? = null,
    val leatherArmorColor: Color? = null,
    val bannerPatterns: List<Pattern> = emptyList(),
    val armorTrim: ArmorTrim? = null,
    val potionColor: Color? = null,
    val merge: FastItemStack? = null,
) {
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
            prepareCustomModelData()?.let { meta.setCustomModelData(it) }
            meta.applyEnchants()
            meta.addItemFlags(*prepareFlags().toTypedArray())
            prepareUnbreakable()?.let { meta.isUnbreakable = it }
            meta.applyGlowing()
            meta.applyDamageIfPresent()
            meta.applySkullTextureIfPresent()
            meta.applyLeatherArmorColorIfPresent()
            meta.applyBannerPatternsIfPresent()
            meta.applyArmorTrimIfPresent()
            meta.applyPotionColorIfPresent()
        }
        itemStack.applyNbtTags()
        return itemStack
    }

    private fun prepareMaterial() = merge?.material ?: material!!

    private fun prepareAmount() = merge?.amount ?: amount ?: 1

    private fun prepareDisplayNameComponent(placeholders: List<Placeholder>): Component? {
        val component =
            merge?.displayName?.mdc(placeholders + listOf("current" to (this@FastItemStack.displayName ?: "")))
                ?: this@FastItemStack.displayName?.mdc(placeholders)
        return component?.decoration(TextDecoration.ITALIC, false)
    }

    private fun prepareLoreComponent(placeholders: List<Placeholder>): List<Component>? {
        val loreComponents =
            merge?.lore?.trimEnd('\n', ' ')?.mdcList(placeholders + listOf("current" to (lore ?: ""))) ?: lore?.trimEnd(
                '\n',
                ' '
            )?.mdcList(placeholders)
        return loreComponents?.map { it.decoration(TextDecoration.ITALIC, false) }
    }

    private fun prepareCustomModelData() = merge?.customModelData ?: customModelData

    private fun ItemMeta.applyEnchants() {
        prepareEnchants().forEach { (name, value) ->
            val enchantment = requireNotNull(Enchantment.getByKey(NamespacedKey.minecraft(name))) {
                "Unknown enchantment: $name"
            }
            addEnchant(enchantment, value, true)
        }
    }

    private fun prepareEnchants() = merge?.enchants ?: enchants

    private fun prepareFlags() = merge?.flags ?: flags

    private fun prepareUnbreakable() = merge?.isUnbreakable ?: isUnbreakable

    private fun ItemMeta.applyGlowing() {
        if (prepareGlowing() == true) {
            addEnchant(Enchantment.DURABILITY, 1, true)
            addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }
    }

    private fun prepareGlowing() = merge?.isGlowing ?: isGlowing

    private fun ItemMeta.applyDamageIfPresent() {
        (this as? Damageable)?.damage = prepareDamage() ?: return
    }

    private fun prepareDamage() = merge?.damage ?: damage

    private fun ItemMeta.applySkullTextureIfPresent() {
        val texture = prepareSkullTexture() ?: return
        if (this !is SkullMeta) return
        if (texture.length <= MINECRAFT_USERNAME_MAX_LENGTH) return applyPlayerHead(texture)
        playerProfile = Bukkit.createProfile(UUID.randomUUID()).apply {
            setProperty(ProfileProperty(TEXTURES_PROPERTY_KEY, texture))
        }
    }

    private fun SkullMeta.applyPlayerHead(playerName: String) {
        owningPlayer = Bukkit.getOfflinePlayer(playerName)
    }

    private fun prepareSkullTexture() = merge?.skullTexture ?: skullTexture

    private fun ItemMeta.applyLeatherArmorColorIfPresent() {
        (this as? LeatherArmorMeta)?.setColor(prepareLeatherArmorColor())
    }

    private fun prepareLeatherArmorColor() = merge?.leatherArmorColor ?: leatherArmorColor

    private fun ItemMeta.applyBannerPatternsIfPresent() {
        (this as? BannerMeta)?.patterns = prepareBannerPatterns()
    }

    private fun prepareBannerPatterns() = merge?.bannerPatterns ?: bannerPatterns

    private fun ItemMeta.applyArmorTrimIfPresent() {
        (this as? ArmorMeta)?.trim = prepareArmorTrim()
    }

    private fun prepareArmorTrim() = merge?.armorTrim ?: armorTrim

    private fun ItemMeta.applyPotionColorIfPresent() {
        (this as? PotionMeta)?.color = preparePotionColor()
    }

    private fun preparePotionColor() = merge?.potionColor ?: potionColor

    private fun ItemStack.applyNbtTags() {
        val nbtItem = NBTItem(this)
        prepareNbtTags().forEach { (key, value) ->
            when (value) {
                is String -> nbtItem.setString(key, value)
                is Byte -> nbtItem.setByte(key, value)
                is ByteArray -> nbtItem.setByteArray(key, value)
                is Short -> nbtItem.setShort(key, value)
                is Int -> nbtItem.setInteger(key, value)
                is IntArray -> nbtItem.setIntArray(key, value)
                is Long -> nbtItem.setLong(key, value)
                is LongArray -> nbtItem.setLongArray(key, value)
                is Float -> nbtItem.setFloat(key, value)
                is Double -> nbtItem.setDouble(key, value)
                is Boolean -> nbtItem.setBoolean(key, value)
                else -> throw IllegalArgumentException("Unsupported NBT type: ${value::class.simpleName}")
            }
        }

        nbtItem.applyNBT(this)
    }

    private fun prepareNbtTags() = merge?.nbtTags ?: nbtTags
}

private const val MINECRAFT_USERNAME_MAX_LENGTH = 16
private const val TEXTURES_PROPERTY_KEY = "textures"
