package specs

import getLibs
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency

interface LibrarySpec {
    val roomRuntime: MinimalExternalModuleDependency
    val roomCompiler: MinimalExternalModuleDependency
    val sqliteBundled: MinimalExternalModuleDependency
    val koinCore: MinimalExternalModuleDependency

    val kotlinTest: MinimalExternalModuleDependency
    val coroutinesSwing: MinimalExternalModuleDependency
    val activityCompose: MinimalExternalModuleDependency
    val lifecycleRuntimeCompose: MinimalExternalModuleDependency
    val lifecycleViewModel: MinimalExternalModuleDependency
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

        override val coroutinesSwing: MinimalExternalModuleDependency
            get() = libs.findLibrary("kotlinx.coroutinesSwing").get().get()

        override val kotlinTest: MinimalExternalModuleDependency
            get() = libs.findLibrary("kotlin.test").get().get()

        override val activityCompose: MinimalExternalModuleDependency
            get() = libs.findLibrary("androidx.activity.compose").get().get()

        override val lifecycleRuntimeCompose: MinimalExternalModuleDependency
            get() = libs.findLibrary("androidx.lifecycle.runtimeCompose").get().get()

        override val lifecycleViewModel: MinimalExternalModuleDependency
            get() = libs.findLibrary("androidx.lifecycle.viewmodel").get().get()
    }