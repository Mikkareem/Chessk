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

        extensions.configure(ComposeExtension::class.java) {
            extensions.configure(DesktopExtension::class.java) {
                application {
                    val baseApplicationId = ConfigurationKeys.baseApplicationId
                    mainClass = "$baseApplicationId.MainKt"

                    nativeDistributions {
                        targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
                        packageName = ConfigurationKeys.Desktop.packageName
                        packageVersion = ConfigurationKeys.Version.versionName
                    }
                }
            }
        }

    }
}