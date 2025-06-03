import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.plugin.use.PluginDependency

interface VersionSpec {
    val androidMinSdk: VersionConstraint
    val androidCompileSdk: VersionConstraint
    val androidTargetSdk: VersionConstraint
}

interface LibrarySpec {
    val roomRuntime: MinimalExternalModuleDependency
    val roomCompiler: MinimalExternalModuleDependency
    val sqliteBundled: MinimalExternalModuleDependency
    val koinCore: MinimalExternalModuleDependency
}

interface PluginSpec {
    val room: PluginDependency
    val ksp: PluginDependency
    val kotlinMultiplatform: PluginDependency
    val androidLibrary: PluginDependency
    val chesskKotlinMultiplatform: PluginDependency
    val chesskAndroidLibrary: PluginDependency
}

val Project.versionSpec: VersionSpec
    get() = object : VersionSpec {
        private val libs = getLibs()

        override val androidMinSdk: VersionConstraint
            get() = libs.findVersion("android.minSdk").get()
        override val androidCompileSdk: VersionConstraint
            get() = libs.findVersion("android.compileSdk").get()
        override val androidTargetSdk: VersionConstraint
            get() = libs.findVersion("android.targetSdk").get()
    }

val Project.librarySpec: LibrarySpec
    get() = object : LibrarySpec {
        private val libs = getLibs()

        override val roomRuntime: MinimalExternalModuleDependency
            get() = libs.findLibrary("androidx.room.runtime").get().get()

        override val roomCompiler: MinimalExternalModuleDependency
            get() = libs.findLibrary("androidx.room.compiler").get().get()

        override val sqliteBundled: MinimalExternalModuleDependency
            get() = libs.findLibrary("sqlite-bundled").get().get()

        override val koinCore: MinimalExternalModuleDependency
            get() = libs.findLibrary("koin.core").get().get()
    }

val Project.pluginSpec: PluginSpec
    get() = object : PluginSpec {
        private val libs = getLibs()

        override val room: PluginDependency
            get() = libs.findPlugin("room").get().get()

        override val ksp: PluginDependency
            get() = libs.findPlugin("ksp").get().get()

        override val kotlinMultiplatform: PluginDependency
            get() = libs.findPlugin("kotlinMultiplatform").get().get()

        override val androidLibrary: PluginDependency
            get() = libs.findPlugin("androidLibrary").get().get()

        override val chesskKotlinMultiplatform: PluginDependency
            get() = libs.findPlugin("chessk.kotlin.multiplatform").get().get()

        override val chesskAndroidLibrary: PluginDependency
            get() = libs.findPlugin("chessk.android.library").get().get()
    }
