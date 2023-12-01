plugins {
    id("eg-libs.base-conventions")
}

dependencies {
    api(projects.egLibsRegistry)
    api(platform(libs.jackson.bom))
    api(libs.jackson.module.kotlin)
    api(libs.jackson.dataformat.yaml)
    implementation(libs.yaml.config.updater)

    compileOnly(paperLibs.adventure.key)
}
