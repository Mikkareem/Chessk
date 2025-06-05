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

            implementation(projects.core.ui.photoPicker)
            implementation(projects.feature.userDetails.domain)

            implementation(libs.koin.core)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
    }
}