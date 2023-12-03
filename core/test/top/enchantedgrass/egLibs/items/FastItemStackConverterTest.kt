package top.enchantedgrass.egLibs.items

import de.themoep.minedown.adventure.MineDown
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemFactory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.*

@ExtendWith(MockKExtension::class)
class FastItemStackConverterTest {
    @MockK
    lateinit var bukkitItemFactory: ItemFactory

    @MockK
    lateinit var itemMeta: ItemMeta

    @Suppress("UnstableApiUsage")
    @BeforeEach
    fun setUp() {
        mockkStatic(Bukkit::class)
        every { Bukkit.getItemFactory() } returns bukkitItemFactory

        every { bukkitItemFactory.getItemMeta(any()) } returns itemMeta
        every { bukkitItemFactory.isApplicable(any(), any<Material>()) } returns true
        every { bukkitItemFactory.asMetaFor(any(), any<Material>()) } answers { firstArg() }
        every { bukkitItemFactory.updateMaterial(any(), any<Material>()) } answers { secondArg() }

        justRun { itemMeta.displayName(null) }
        justRun { itemMeta.lore(null) }
        every { itemMeta.clone() } returns itemMeta

        val displayNameSlot = slot<Component>()
        justRun { itemMeta.displayName(capture(displayNameSlot)) }
        every { itemMeta.displayName() } answers { displayNameSlot.captured }
    }

    @AfterEach
    fun tearDown() {
        clearStaticMockk(Bukkit::class)
    }

    @Test
    fun `asBukkit returns correct ItemStack when no merge`() {
        val itemFlags = mutableListOf<ItemFlag>()
        justRun { itemMeta.addItemFlags(capture(itemFlags)) }
        every { itemMeta.hasItemFlag(any()) } answers { itemFlags.contains(firstArg()) }

        val unbreakableSlot = slot<Boolean>()
        justRun { itemMeta.isUnbreakable = capture(unbreakableSlot) }
        every { itemMeta.isUnbreakable } answers { unbreakableSlot.captured }

        val fastItemStack = FastItemStack(
            material = Material.DIAMOND,
            amount = 5,
            displayName = "&cTest Item",
            isUnbreakable = true,
            flags = mutableSetOf(ItemFlag.HIDE_ENCHANTS)
        )

        val converter = FastItemStackConverter(fastItemStack)
        val itemStack = converter.asBukkit()

        assertEquals(Material.DIAMOND, itemStack.type)
        assertEquals(5, itemStack.amount)
        assertNotNull(itemStack.itemMeta)
        assertEquals("&cTest Item", itemStack.itemMeta.displayName()?.let { MineDown.stringify(it) })
        assertTrue(itemStack.itemMeta.isUnbreakable)
        assertTrue(itemStack.itemMeta.hasItemFlag(ItemFlag.HIDE_ENCHANTS))
    }

    @Test
    fun `asBukkit returns correct ItemStack when merge present`() {
        val unbreakableSlot = slot<Boolean>()
        justRun { itemMeta.isUnbreakable = capture(unbreakableSlot) }
        every { itemMeta.isUnbreakable } answers { unbreakableSlot.captured }

        val mergeFastItemStack = FastItemStack(
            material = Material.GOLD_INGOT,
            amount = 10,
            displayName = "Prefix %current%",
            isUnbreakable = false
        )

        val fastItemStack = FastItemStack(
            material = Material.DIAMOND,
            amount = 5,
            displayName = "Test Item",
            isUnbreakable = true,
            merge = mergeFastItemStack
        )

        val converter = FastItemStackConverter(fastItemStack)
        val itemStack = converter.asBukkit()

        assertEquals(Material.GOLD_INGOT, itemStack.type)
        assertEquals(10, itemStack.amount)
        assertEquals("Prefix Test Item", itemStack.itemMeta?.displayName()?.let { MineDown.stringify(it) })
        assertFalse(itemStack.itemMeta?.isUnbreakable!!)
    }

    @Test
    fun `asBukkit throws when material is null`() {
        val fastItemStack = FastItemStack()

        val converter = FastItemStackConverter(fastItemStack)

        assertFailsWith<NullPointerException> {
            converter.asBukkit(emptyList())
        }
    }
}
