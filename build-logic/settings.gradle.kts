@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        register("libs") {
            from(files("../version-catalogs/libs.versions.toml"))
        }
    }
}

rootProject.name = "eg-libs-build-logic"
