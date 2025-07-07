plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
}

kotlin {
    sourceSets.commonMain.dependencies {
        implementation(libs.kotlinx.coroutines.core)
    }
}