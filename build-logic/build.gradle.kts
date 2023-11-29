plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.kotlin.gradle.plugin)

    compileOnly(files(libs::class.java.protectionDomain.codeSource.location))
}
