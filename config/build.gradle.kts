plugins {
    id("eg-libs.base-conventions")
}

dependencies {
    implementation(projects.egLibsRegistry)
    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.yaml.config.updater)

    compileOnly(paperLibs.adventure.key)
}
