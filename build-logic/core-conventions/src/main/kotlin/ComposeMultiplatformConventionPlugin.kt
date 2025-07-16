import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import specs.pluginSpec

internal class ComposeMultiplatformConventionPlugin: Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        plugins.apply(pluginSpec.composeMultiplatform.pluginId)
        plugins.apply(pluginSpec.composeCompiler.pluginId)
    }
}