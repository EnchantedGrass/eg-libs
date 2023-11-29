plugins {
    id("eg-libs.kotlin-conventions")
}

dependencies {
    compileOnly(platformLibs.paper.api)

    api(libs.kotlinx.coroutines.core)
    api(libs.bundles.mccoroutine.bukkit)
}
