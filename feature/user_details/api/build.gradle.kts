plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
    alias(libs.plugins.conventions.android.library)
    alias(libs.plugins.conventions.compose.multiplatform)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.userDetails.data)
            implementation(projects.feature.userDetails.domain)
            implementation(projects.feature.userDetails.presentation)

            implementation(libs.koin.core)
            implementation(libs.androidx.navigation.compose)
        }
    }
}