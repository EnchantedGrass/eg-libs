plugins {
    id("eg-libs.base-conventions")
}

val paperLibs: VersionCatalog = versionCatalogs.named("paperLibs")

dependencies {
    paperLibs.findLibrary("paper-api").ifPresent {
        compileOnly(it)
        testImplementation(it)
    }
}
