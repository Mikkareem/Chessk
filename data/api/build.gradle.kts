plugins {
    alias(libs.plugins.conventions.kotlin.multiplatform)
    alias(libs.plugins.conventions.android.library)
}

kotlin {

    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.bundles.ktor.client)
            implementation(libs.koin.core)

            api(projects.base)
            api(projects.shared)
            implementation(projects.data.datastore)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}