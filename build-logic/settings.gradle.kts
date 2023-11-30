@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        val libs by registering {
            from(files("../version-catalogs/libs.versions.toml"))
        }
        val testLibs by registering {
            from(files("../version-catalogs/testLibs.versions.toml"))
        }
    }
}

rootProject.name = "eg-libs-build-logic"
