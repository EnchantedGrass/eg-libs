plugins {
    alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
}

group = property("group")!!
version = property("version")!!
