package top.enchantedgrass.egLibs.items

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.trim.ArmorTrim

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
)
