plugins {
    id("eg-libs.base-conventions")
}

dependencies {
    compileOnly(paperLibs.paper.api)
    compileOnly(projects.egLibsCore)

    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.module.kotlin)
    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.yaml.config.updater)
    implementation(projects.egLibsCore)
    testImplementation(paperLibs.paper.api)
}
