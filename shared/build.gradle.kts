plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.kotlin.serialization)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization.json)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}