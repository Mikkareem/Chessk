import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

object ConfigurationKeys {

    const val baseApplicationId = "com.techullurgy.chessk"

    object Android {
        const val isMinifyEnabled = false
        const val isShrinkResources = false

        val javaVersion = JavaVersion.VERSION_17
        val jvmTarget = JvmTarget.JVM_17
    }

    object Version {
        const val versionCode = 1

        private const val versionMajor = 1
        private const val versionMinor = 0
        private const val versionPatch = 0

        const val versionName = "$versionMajor.$versionMinor.$versionPatch"
    }

    object Desktop {
        const val packageName = "ChessK"
    }
}