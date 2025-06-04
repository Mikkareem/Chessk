import androidx.room.gradle.RoomExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import specs.conventionPluginSpec
import specs.librarySpec
import specs.pluginSpec

class RoomKmpConventionPlugin: Plugin<Project> {
    override fun apply(target: Project): Unit = with(target) {
        plugins.apply(conventionPluginSpec.conventionsKotlinMultiplatform.pluginId)
        plugins.apply(conventionPluginSpec.conventionsAndroidLibrary.pluginId)
        plugins.apply(pluginSpec.ksp.pluginId)
        plugins.apply(pluginSpec.room.pluginId)

        extensions.configure(KotlinMultiplatformExtension::class.java) {
            sourceSets.commonMain.dependencies {
                implementation(librarySpec.roomRuntime)
                implementation(librarySpec.sqliteBundled)
            }
        }

        extensions.configure(RoomExtension::class.java) {
            schemaDirectory("$projectDir/schemas")
        }

        dependencies {
            val roomKspCompiler = librarySpec.roomCompiler

            add("kspAndroid", roomKspCompiler)
            add("kspIosSimulatorArm64", roomKspCompiler)
            add("kspIosX64", roomKspCompiler)
            add("kspIosArm64", roomKspCompiler)
            add("kspDesktop", roomKspCompiler)
        }
    }
}