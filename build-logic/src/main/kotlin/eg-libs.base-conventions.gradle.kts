@file:Suppress("UnstableApiUsage")

plugins {
    id("eg-libs.kotlin-conventions")
}

val libs: VersionCatalog = versionCatalogs.named("libs")
val testLibs: VersionCatalog = versionCatalogs.named("testLibs")

testing {
    suites {
        val test by getting(JvmTestSuite::class) {
            libs.findVersion("kotlin").map { it.preferredVersion }.ifPresent(::useKotlinTest)

            dependencies {
                testLibs.findLibrary("mockk").ifPresent { implementation(it) }
                testLibs.findLibrary("slf4j-simple").ifPresent { implementation(it) }
            }
        }
    }
}
