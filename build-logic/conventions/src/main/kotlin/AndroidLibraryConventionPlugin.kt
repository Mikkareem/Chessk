import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class AndroidLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        plugins.apply(pluginSpec.androidLibrary.pluginId)

        extensions.configure(KotlinMultiplatformExtension::class.java) { targetAndroid() }

        extensions.configure(LibraryExtension::class.java) {
            namespace = "com.techullurgy.chessk.${this@with.modulePackageName}"
            compileSdk = versionSpec.androidCompileSdk.requiredVersion.toInt()

            defaultConfig {
                minSdk = versionSpec.androidMinSdk.requiredVersion.toInt()
            }
            packaging {
                resources {
                    excludes += "/META-INF/{AL2.0,LGPL2.1}"
                }
            }
            buildTypes {
                getByName("release") {
                    isMinifyEnabled = false
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
    }
}