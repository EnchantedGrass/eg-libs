plugins {
    id("eg-libs.paper-conventions")
    id("eg-libs.test-fixtures-conventions")
}

dependencies {
    api(projects.egLibsNbt.apply { targetConfiguration = "shadow" })
    api(libs.kotlinx.coroutines.core)
    api(paperLibs.bundles.mccoroutine.bukkit)
    api(platform(libs.jackson.bom))
    api(libs.jackson.module.kotlin)
    api(libs.jackson.dataformat.yaml)

    implementation(paperLibs.minedown)

    testFixturesCompileOnly(testLibs.mockk)
}
