import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    id("dev.androidbroadcast.navstate.mavenPublish")
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

        macosX64(),
        macosArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "NavStateCompose"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                api(projects.navstateCore)
                implementation(libs.essenty.backhandler)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.activity.compose)
            }
        }
    }
}

android {
    namespace = "dev.androidbroadcast.navstate.compose"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.kotlinCompilerExtensionVersion.get()
    }
}

publishing {
    publications.getByName<MavenPublication>("maven") {
        version = "0.1"
        description = "Navigation library based on state"
    }
}
