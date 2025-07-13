plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
    alias(libs.plugins.conventions.compose.multiplatform)
    alias(libs.plugins.conventions.android.library)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)

            implementation(libs.koin.core)
            implementation(libs.androidx.navigation.compose)

            implementation(projects.feature.joinedRooms.presentation)
            implementation(projects.feature.gameRoom.presentation)
        }
    }
}