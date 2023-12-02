plugins {
    id("eg-libs.kotlin-conventions")
    alias(libs.plugins.shadow)
}

dependencies {
    api(paperLibs.item.nbt.api)
}

tasks.shadowJar {
    relocate("de.tr7zw.changeme.nbtapi", "top.enchantedgrass.egLibs.nbt")
}
