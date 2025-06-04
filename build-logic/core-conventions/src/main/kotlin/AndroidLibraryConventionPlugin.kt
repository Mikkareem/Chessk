import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import specs.pluginSpec
import specs.versionSpec

internal class AndroidLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        plugins.apply(pluginSpec.androidLibrary.pluginId)

        extensions.configure(KotlinMultiplatformExtension::class.java) { targetAndroid() }

        extensions.configure(LibraryExtension::class.java) {
            val baseApplicationId = ConfigurationKeys.baseApplicationId

            namespace = "$baseApplicationId.${modulePackageName}"
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
                    isMinifyEnabled = ConfigurationKeys.Android.isMinifyEnabled
                    isShrinkResources = ConfigurationKeys.Android.isShrinkResources
                }
            }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
    }
}