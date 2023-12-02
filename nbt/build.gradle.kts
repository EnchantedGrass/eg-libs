plugins {
    id("eg-libs.kotlin-conventions")
    alias(libs.plugins.shadow)
}

val itemNbtApi: Configuration by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

dependencies {
    itemNbtApi(paperLibs.item.nbt.api)
}

tasks {
    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    shadowJar {
        configurations = listOf(itemNbtApi)
        relocate("de.tr7zw.changeme.nbtapi", "top.enchantedgrass.egLibs.nbt")
    }
}
