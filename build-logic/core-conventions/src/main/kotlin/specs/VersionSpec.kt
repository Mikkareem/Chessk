package specs

import getLibs
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionConstraint

interface VersionSpec {
    val androidMinSdk: VersionConstraint
    val androidCompileSdk: VersionConstraint
    val androidTargetSdk: VersionConstraint

    val ktlint: VersionConstraint
    val ktlintComposeRules: VersionConstraint
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
        override val ktlint: VersionConstraint
            get() = libs.findVersion("ktlint").get()
        override val ktlintComposeRules: VersionConstraint
            get() = libs.findVersion("ktlint.compose.rules").get()
    }
