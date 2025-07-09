plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.ktor.client)

            implementation(projects.base)
            implementation(projects.shared)
        }
    }
}