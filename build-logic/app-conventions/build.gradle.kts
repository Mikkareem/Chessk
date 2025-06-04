plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(projects.coreConventions)

    dependencies {
        compileOnly(libs.kotlin.gradle.plugin)
        compileOnly(libs.compose.gradle.plugin)
    }
}

gradlePlugin {
    plugins {
        register("composeApp") {
            id = libs.plugins.chessk.compose.app.get().pluginId
            implementationClass = "ChessKApplicationConventionPlugin"
        }
    }
}