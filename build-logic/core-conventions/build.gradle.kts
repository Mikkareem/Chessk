plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.room.gradle.plugin)
    compileOnly(libs.compose.gradle.plugin)
    compileOnly(libs.spotless.gradle.plugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = libs.plugins.conventions.kotlin.multiplatform.get().pluginId
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }

        register("androidLibrary") {
            id = libs.plugins.conventions.android.library.get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidApplication") {
            id = libs.plugins.conventions.android.application.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("composeMultiplatform") {
            id = libs.plugins.conventions.compose.multiplatform.get().pluginId
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }

        register("roomKmp") {
            id = libs.plugins.conventions.kmp.room.get().pluginId
            implementationClass = "RoomKmpConventionPlugin"
        }

        register("spotless") {
            id = libs.plugins.conventions.spotless.get().pluginId
            implementationClass = "SpotlessConventionPlugin"
        }
    }
}