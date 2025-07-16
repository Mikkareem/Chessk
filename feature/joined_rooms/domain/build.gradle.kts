plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)

            implementation(projects.feature.game.data)
            implementation(projects.feature.game.models)
        }
    }
}