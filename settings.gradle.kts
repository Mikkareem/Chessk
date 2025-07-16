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
include(":core:ui:components")
include(":core:ui:photo_picker")
include(":core:ui:snackbar")

include(":feature:game:models")
include(":feature:game:data")
include(":feature:rooms:data")
include(":feature:user_details:data")

include(":feature:splash:domain")
include(":feature:splash:presentation")

include(":feature:register:domain")
include(":feature:register:presentation")

include(":feature:login:domain")
include(":feature:login:presentation")

include(":feature:menu:presentation")

include(":feature:create_room:domain")
include(":feature:create_room:presentation")

include(":feature:created_rooms:domain")
include(":feature:created_rooms:presentation")

include(":feature:join_room:domain")
include(":feature:join_room:presentation")

include(":feature:joined_rooms:domain")
include(":feature:joined_rooms:presentation")

include(":feature:game_room:domain")
include(":feature:game_room:presentation")