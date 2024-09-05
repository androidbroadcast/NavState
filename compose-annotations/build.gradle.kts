@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
//    id("dev.androidbroadcast.navstate.mavenPublish")
}

group = "dev.androidbroadcast.navstate"
version = "0.1"

kotlin {
    explicitApi = ExplicitApiMode.Strict

    jvm()

    androidTarget {
        publishLibraryVariants("release")

        compilations.all {
            compileTaskProvider {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                    freeCompilerArgs.add("-Xjdk-release=${JavaVersion.VERSION_17}")
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),

        macosArm64(),
        macosX64(),
    ).forEach {
        it.binaries.framework {
            baseName = "NavStateComposeAnnotations"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.navstateCompose)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

android {
    namespace = "dev.androidbroadcast.navstate.annotations"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

//publishing {
//    publications.getByName<MavenPublication>("maven") {
//        version = "0.1"
//        description = "Annotations for declarative NavState processor"
//    }
//}
