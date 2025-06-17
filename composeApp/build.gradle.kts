plugins {
    alias(libs.plugins.chessk.compose.app)
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(projects.navigation)
            implementation(projects.database)
            implementation(projects.core.remote)
            implementation(projects.core.ui.snackbar)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
