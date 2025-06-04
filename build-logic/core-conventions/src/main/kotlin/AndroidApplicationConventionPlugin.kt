import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import specs.pluginSpec
import specs.versionSpec

internal class AndroidApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        plugins.apply(pluginSpec.androidApplication.pluginId)

        extensions.configure(KotlinMultiplatformExtension::class.java) { targetAndroid() }

        extensions.configure(BaseAppModuleExtension::class.java) {
            val baseApplicationId = ConfigurationKeys.baseApplicationId

            namespace = baseApplicationId
            compileSdk = versionSpec.androidCompileSdk.requiredVersion.toInt()

            defaultConfig {
                applicationId = baseApplicationId
                minSdk = versionSpec.androidMinSdk.requiredVersion.toInt()
                targetSdk = versionSpec.androidTargetSdk.requiredVersion.toInt()
                versionCode = ConfigurationKeys.Version.versionCode
                versionName = ConfigurationKeys.Version.versionName
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
                sourceCompatibility = ConfigurationKeys.Android.javaVersion
                targetCompatibility = ConfigurationKeys.Android.javaVersion
            }
        }
    }
}