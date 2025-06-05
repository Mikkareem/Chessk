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

            implementation(libs.kotlinx.coroutines.core)
        }
    }
}