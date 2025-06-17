plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.models)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}