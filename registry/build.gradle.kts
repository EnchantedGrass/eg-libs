plugins {
    id("eg-libs.base-conventions")
}

dependencies {
    compileOnly(paperLibs.adventure.key)
    testImplementation(paperLibs.adventure.key)
}
