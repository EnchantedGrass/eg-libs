package top.enchantedgrass.egLibs.config.yaml

data class TestConfigObject(
    val a: Int?,
    val b: String?,
    val c: Boolean?,
    val d: List<String> = emptyList(),
    val e: Map<String, Int> = emptyMap()
)
