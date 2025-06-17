plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
    alias(libs.plugins.conventions.compose.multiplatform)
    alias(libs.plugins.conventions.android.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.androidx.navigation.compose)

            implementation(projects.feature.userDetails.api)
            implementation(projects.feature.game.api)
        }
    }
}