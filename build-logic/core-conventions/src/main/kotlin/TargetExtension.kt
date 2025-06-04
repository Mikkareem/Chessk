import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun KotlinMultiplatformExtension.targetAndroid() {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(ConfigurationKeys.Android.jvmTarget)
        }
    }
}

internal fun KotlinMultiplatformExtension.targetIOS(project: Project) {
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = project.moduleName
            isStatic = true
        }
    }
}

internal fun KotlinMultiplatformExtension.targetDesktop() {
    jvm("desktop")
}