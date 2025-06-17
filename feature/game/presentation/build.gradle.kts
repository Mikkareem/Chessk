plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
    alias(libs.plugins.conventions.android.library)
    alias(libs.plugins.conventions.compose.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)

            // TODO: Temporary: Actual 'Domain' not 'Data'
            implementation(projects.feature.game.data)


            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}