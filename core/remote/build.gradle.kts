plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
    alias(libs.plugins.conventions.android.library)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.ktor.client)
            implementation(libs.koin.core)

            implementation(projects.core.constants)
            implementation(projects.shared)
        }
    }
}