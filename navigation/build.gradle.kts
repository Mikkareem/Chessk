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

            implementation(projects.feature.splash.presentation)
            implementation(projects.feature.register.presentation)
            implementation(projects.feature.login.presentation)
            implementation(projects.feature.menu.presentation)
            implementation(projects.feature.createRoom.presentation)
            implementation(projects.feature.createdRooms.presentation)
            implementation(projects.feature.joinRoom.presentation)
            implementation(projects.feature.joinedRooms.presentation)
            implementation(projects.feature.gameRoom.presentation)
        }
    }
}