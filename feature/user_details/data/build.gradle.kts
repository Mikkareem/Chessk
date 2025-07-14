plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)

            implementation(projects.data.datastore)
            implementation(projects.data.remote)
        }
    }
}