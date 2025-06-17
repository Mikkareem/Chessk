plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
}

kotlin {
    sourceSets.commonMain.dependencies {
        api(projects.shared)
        implementation(projects.core.constants)
    }
}