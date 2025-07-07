plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
    alias(libs.plugins.conventions.android.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(projects.feature.userDetails.domain)
            implementation(projects.core.constants)

            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.ktor.client.core)
        }
    }
}