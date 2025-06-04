import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import specs.conventionPluginSpec
import specs.librarySpec

class ChessKApplicationConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) : Unit = with(target) {
        plugins.apply(conventionPluginSpec.conventionsComposeMultiplatform.pluginId)
        plugins.apply(conventionPluginSpec.conventionsKotlinMultiplatform.pluginId)
        plugins.apply(conventionPluginSpec.conventionsAndroidApplication.pluginId)

        val composeDeps = extensions.getByType<ComposeExtension>().dependencies

        extensions.configure(KotlinMultiplatformExtension::class.java) {

            sourceSets.commonMain.dependencies {
                implementation(composeDeps.runtime)
                implementation(composeDeps.foundation)
                implementation(composeDeps.material3)
                implementation(composeDeps.ui)
                implementation(composeDeps.components.resources)
                implementation(composeDeps.components.uiToolingPreview)
                implementation(librarySpec.lifecycleViewModel)
                implementation(librarySpec.lifecycleRuntimeCompose)
            }

            sourceSets.androidMain.dependencies {
                implementation(composeDeps.preview)
                implementation(librarySpec.activityCompose)
            }

            sourceSets.commonTest.dependencies {
                implementation(librarySpec.kotlinTest)
            }

            sourceSets.named("desktopMain").dependencies {
                implementation(composeDeps.desktop.currentOs)
                implementation(librarySpec.coroutinesSwing)
            }
        }
    }
}