import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformConventionPlugin: Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {

        plugins.apply(pluginSpec.kotlinMultiplatform.pluginId)

        extensions.configure(KotlinMultiplatformExtension::class.java) {
            targetIOS(this@with)
            targetDesktop()

            sourceSets.commonMain.dependencies {
                implementation(librarySpec.koinCore)
            }
        }
    }
}