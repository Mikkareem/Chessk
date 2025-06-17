plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.database)

            implementation(projects.feature.game.domain)
            implementation(libs.ktor.client.core)
        }
    }
}