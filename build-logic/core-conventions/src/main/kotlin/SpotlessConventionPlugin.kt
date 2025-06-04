import com.diffplug.gradle.spotless.SpotlessExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import specs.pluginSpec
import specs.versionSpec

class SpotlessConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        val ktlintVersion = target.versionSpec.ktlint.requiredVersion
        val ktlintComposeRulesVersion = target.versionSpec.ktlintComposeRules.requiredVersion

        val spotlessPluginId = target.pluginSpec.spotless.pluginId

        target.subprojects {
            plugins.apply(spotlessPluginId)

            // https://pinterest.github.io/ktlint/latest/
            // https://mrmans0n.github.io/compose-rules/rules/

            extensions.configure(SpotlessExtension::class.java) {
                kotlin {
                    target("src/**/*.kt")
                    targetExclude("build/**/*.kt")
                    ktlint(ktlintVersion)
                        .editorConfigOverride(
                            mapOf(
                                "indent_size" to 4
                            )
                        )
                        .customRuleSets(
                            listOf("io.nlopez.compose.rules:ktlint:$ktlintComposeRulesVersion")
                        )
                }

                kotlinGradle {
                    target("*.kts")
                    ktlint(ktlintVersion)
                }
            }
        }
    }
}