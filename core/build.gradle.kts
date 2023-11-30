plugins {
    id("eg-libs.paper-conventions")
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(paperLibs.bundles.mccoroutine.bukkit)
}
