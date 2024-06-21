@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")

    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "NavState"
include(":compose")
project(":compose").name = "navstate-compose"

include(":core")
project(":core").name = "navstate-core"

include(":compose-annotations")
project(":compose-annotations").name = "navstate-compose-annotations"

include(":samples:android-compose")

include(":compose-processor")
project(":compose-processor").name = "navstate-compose-processor"
