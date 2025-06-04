package specs

import getLibs
import org.gradle.api.Project
import org.gradle.plugin.use.PluginDependency

interface PluginSpec {
    val room: PluginDependency
    val ksp: PluginDependency
    val kotlinMultiplatform: PluginDependency
    val composeMultiplatform: PluginDependency
    val composeCompiler: PluginDependency
    val androidApplication: PluginDependency
    val androidLibrary: PluginDependency
    val spotless: PluginDependency
}

internal val Project.pluginSpec: PluginSpec
    get() = object : PluginSpec {
        private val libs = getLibs()

        override val room: PluginDependency
            get() = libs.findPlugin("room").get().get()

        override val ksp: PluginDependency
            get() = libs.findPlugin("ksp").get().get()

        override val kotlinMultiplatform: PluginDependency
            get() = libs.findPlugin("kotlinMultiplatform").get().get()

        override val composeMultiplatform: PluginDependency
            get() = libs.findPlugin("composeMultiplatform").get().get()

        override val composeCompiler: PluginDependency
            get() = libs.findPlugin("composeCompiler").get().get()

        override val androidApplication: PluginDependency
            get() = libs.findPlugin("androidApplication").get().get()

        override val androidLibrary: PluginDependency
            get() = libs.findPlugin("androidLibrary").get().get()

        override val spotless: PluginDependency
            get() = libs.findPlugin("spotless").get().get()
    }
