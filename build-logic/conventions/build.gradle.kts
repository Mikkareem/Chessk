plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.room.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

//gradlePlugin {
//    plugins {
//        register("androidLibrary") {
//            id = libs.plugins.temp.android.library.get().pluginId
//            implementationClass = "AndroidLibraryConventionPlugin"
//        }
//    }
//}