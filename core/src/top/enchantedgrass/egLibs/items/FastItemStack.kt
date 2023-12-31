package top.enchantedgrass.egLibs.items

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.block.banner.Pattern
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.trim.ArmorTrim
import top.enchantedgrass.egLibs.jackson.BukkitArmorTrim
import top.enchantedgrass.egLibs.jackson.BukkitColor


/**
 * The FastItemStack class represents a fast and efficient way to create and manipulate ItemStack objects in Bukkit.
 *
 * @property material The Material of the ItemStack.
 * @property amount The number of items in the ItemStack.
 * @property displayName The display name of the ItemStack.
 * @property lore The lore of the ItemStack.
 * @property customModelData The custom model data of the ItemStack.
 * @property enchants The enchantments applied to the ItemStack.
 * @property flags The flags applied to the ItemStack.
 * @property isUnbreakable Indicates if the ItemStack is unbreakable.
 * @property isGlowing Indicates if the ItemStack is glowing.
 * @property nbtTags The NBT tags of the ItemStack.
 * @property damage The damage value of the ItemStack.
 * @property skullTexture The skull texture of the ItemStack.
 * @property leatherArmorColor The leather armor color of the ItemStack.
 * @property bannerPatterns The banner patterns of the ItemStack.
 * @property armorTrim The armor trim of the ItemStack.
 * @property potionColor The potion color of the ItemStack.
 * @property merge The ItemStack to merge with.
 *
 * @constructor Creates a new FastItemStack instance.
 */
data class FastItemStack(
    val material: Material? = null,
    val amount: Int? = null,
    val displayName: String? = null,
    val lore: String? = null,
    val customModelData: Int? = null,
    val enchants: Map<String, Int> = emptyMap(),
    val flags: Set<ItemFlag> = emptySet(),
    val isUnbreakable: Boolean? = null,
    val isGlowing: Boolean? = null,
    val nbtTags: Map<String, Any> = emptyMap(),
    val damage: Int? = null,
    val skullTexture: String? = null,
    val leatherArmorColor: Color? = null,
    val bannerPatterns: List<Pattern> = emptyList(),
    @JsonSerialize(using = BukkitArmorTrim.Serializer::class)
    @JsonDeserialize(using = BukkitArmorTrim.Deserializer::class)
    val armorTrim: ArmorTrim? = null,
    @JsonSerialize(using = BukkitColor.Serializer::class)
    @JsonDeserialize(using = BukkitColor.Deserializer::class)
    val potionColor: Color? = null,
    val merge: FastItemStack? = null,
)
