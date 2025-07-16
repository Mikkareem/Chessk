plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines.core)

            implementation(projects.data.database)
            implementation(projects.data.remote)
            implementation(projects.data.websockets)

            implementation(projects.feature.game.models)
        }
    }
}