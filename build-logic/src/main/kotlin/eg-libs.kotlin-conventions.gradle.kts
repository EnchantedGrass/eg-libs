plugins {
    kotlin("jvm")
}

val jdkVersion = prop("jdk.version")
kotlin {
    sourceSets {
        all {
            languageSettings {
                languageVersion = "2.0"
            }
        }
        main {
            kotlin.setSrcDirs(listOf("src"))
            resources.setSrcDirs(listOf("resources"))
        }
    }
}

java {
    toolchain {
        languageVersion = jdkVersion.map { JavaLanguageVersion.of(it) }
        vendor = JvmVendorSpec.AZUL
    }
}
