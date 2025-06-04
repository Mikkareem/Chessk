import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import specs.pluginSpec

internal class KotlinMultiplatformConventionPlugin: Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {

        plugins.apply(pluginSpec.kotlinMultiplatform.pluginId)

        extensions.configure(KotlinMultiplatformExtension::class.java) {
            targetIOS(this@with)
            targetDesktop()
        }
    }
}