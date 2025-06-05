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
            implementation(projects.core.remote)

            implementation(libs.koin.core)
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
