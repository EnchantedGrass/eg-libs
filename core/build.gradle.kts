plugins {
    id("eg-libs.paper-conventions")
    id("eg-libs.test-fixtures-conventions")
}

dependencies {
    api(libs.kotlinx.coroutines.core)
    api(paperLibs.bundles.mccoroutine.bukkit)

    implementation(paperLibs.minedown)

    testFixturesCompileOnly(testLibs.mockk)
}
