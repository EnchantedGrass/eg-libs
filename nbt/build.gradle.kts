plugins {
    id("eg-libs.kotlin-conventions")
    alias(libs.plugins.shadow)
}

dependencies {
    shadow(paperLibs.item.nbt.api)
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    shadowJar {
        configurations = listOf(project.configurations.shadow.get())
        relocate("de.tr7zw.changeme.nbtapi", "top.enchantedgrass.egLibs.nbt")
    }
}
