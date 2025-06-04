package specs

import getLibs
import org.gradle.api.Project
import org.gradle.plugin.use.PluginDependency

interface ConventionPluginSpec {
    val conventionsKotlinMultiplatform: PluginDependency
    val conventionsAndroidLibrary: PluginDependency
    val conventionsComposeMultiplatform: PluginDependency
    val conventionsKmpRoomLibrary: PluginDependency
    val conventionsSpotless: PluginDependency
    val conventionsAndroidApplication: PluginDependency
}

val Project.conventionPluginSpec: ConventionPluginSpec
    get() = object : ConventionPluginSpec {
        private val libs = getLibs()

        override val conventionsKotlinMultiplatform: PluginDependency
            get() = libs.findPlugin("conventions.kotlin.multiplatform").get().get()

        override val conventionsAndroidLibrary: PluginDependency
            get() = libs.findPlugin("conventions.android.library").get().get()

        override val conventionsAndroidApplication: PluginDependency
            get() = libs.findPlugin("conventions.android.application").get().get()

        override val conventionsComposeMultiplatform: PluginDependency
            get() = libs.findPlugin("conventions.compose.multiplatform").get().get()

        override val conventionsKmpRoomLibrary: PluginDependency
            get() = libs.findPlugin("conventions.kmp.room").get().get()

        override val conventionsSpotless: PluginDependency
            get() = libs.findPlugin("conventions.spotless").get().get()
    }