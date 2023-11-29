@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://www.jitpack.io")
        maven("https://repo.papermc.io/repository/maven-public/")
    }
    versionCatalogs {
        create("platformLibs") {
            from(files("gradle/platformLibs.versions.toml"))
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "eg-libs"

setOf("core").forEach {
    val subprojectName = ":eg-libs-$it"
    include(subprojectName)
    project(subprojectName).projectDir = file(it)
}
