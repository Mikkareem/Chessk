rootProject.name = "ChessK"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

includeBuild("build-logic")

// Server
include(":server")

// Shared between Server and Client (DTOs and Commons)
include(":shared")

// Client
include(":composeApp")

include(":navigation")

include(":base")

include(":data:api")
include(":data:database")
include(":data:datastore")
include(":data:remote")
include(":data:websockets")

include(":core:constants")
include(":core:ui:photo_picker")
include(":core:ui:snackbar")

include(":feature:game:data")
include(":feature:game:models")