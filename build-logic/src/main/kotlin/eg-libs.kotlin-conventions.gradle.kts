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
        test {
            kotlin.setSrcDirs(listOf("test"))
            resources.setSrcDirs(listOf("testResources"))
        }
    }
}

java {
    toolchain {
        languageVersion = jdkVersion.map { JavaLanguageVersion.of(it) }
        vendor = JvmVendorSpec.AZUL
    }
}

sourceSets {
    main {
        java.setSrcDirs(listOf("src"))
        resources.srcDirs("resources")
    }
    test {
        java.setSrcDirs(listOf("test"))
        resources.srcDirs("testResources")
    }
}
