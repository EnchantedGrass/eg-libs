@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
    repositories {
        mavenCentral()
        maven("https://www.jitpack.io")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
    versionCatalogs {
        val libs by creating {
            from(files("version-catalogs/libs.versions.toml"))
        }
        val paperLibs by creating {
            from(files("version-catalogs/paperLibs.versions.toml"))
        }
        val testLibs by creating {
            from(files("version-catalogs/testLibs.versions.toml"))
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "eg-libs"

setOf("registry", "core", "config").forEach {
    val subprojectName = ":eg-libs-$it"
    include(subprojectName)
    project(subprojectName).projectDir = file(it)
}
