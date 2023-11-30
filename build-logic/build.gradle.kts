plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(files(libs::class.java.protectionDomain.codeSource.location))
    compileOnly(files(testLibs::class.java.protectionDomain.codeSource.location))

    implementation(libs.kotlin.gradle.plugin)
}
