plugins {
    alias(libs.plugins.chessk.compose.app)
}

//kotlin {
//    sourceSets {
//        val desktopMain by getting
//
//        androidMain.dependencies {
//            implementation(compose.preview)
//            implementation(libs.androidx.activity.compose)
//        }
//        commonMain.dependencies {
//            implementation(compose.runtime)
//            implementation(compose.foundation)
//            implementation(compose.material3)
//            implementation(compose.ui)
//            implementation(compose.components.resources)
//            implementation(compose.components.uiToolingPreview)
//            implementation(libs.androidx.lifecycle.viewmodel)
//            implementation(libs.androidx.lifecycle.runtimeCompose)
//        }
//        commonTest.dependencies {
//            implementation(libs.kotlin.test)
//        }
//        desktopMain.dependencies {
//            implementation(compose.desktop.currentOs)
//            implementation(libs.kotlinx.coroutinesSwing)
//        }
//    }
//}

dependencies {
    debugImplementation(compose.uiTooling)
}
