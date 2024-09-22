import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.mavenPublish.vanniktech)
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
                    jvmTarget.set(JvmTarget.JVM_21)
                    freeCompilerArgs.add("-Xjdk-release=${JavaVersion.VERSION_21}")
                }
            }
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
        tvosArm64(),
        tvosX64(),
        tvosSimulatorArm64(),
        watchosArm32(),
        watchosArm64(),
        watchosX64(),
        macosX64(),
        macosArm64(),
    ).forEach {
        it.binaries.framework {
            baseName = "NavStateComposeCore"
            isStatic = true
        }
    }

    linuxX64()
    linuxArm64()

    mingwX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.serialization.core)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

android {
    namespace = "io.github.androidbroadcast"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

publishing {
    publications
        .withType<MavenPublication>()
        .configureEach {
            groupId = "dev.androidbroadcast.navstate"

            pom {
                description = "Navigation library based on state"
                url = "https://github.com/androidbroadcast/NavState"

                licenses {
                    license {
                        name = "The Apache License, Version 2.0"
                        url = "https://github.com/androidbroadcast/NavState/blob/main/LICENSE"
                    }
                }

                developers {
                    developer {
                        id = "kirich1409"
                        name = "Kirill Rozov"
                        email = "kirill@androidbroadcast.dev"
                        organization = "Anroid Broadcast"
                        organizationUrl = "https://github.com/androidbroadcast"
                    }
                }

                organization {
                    name = "Anroid Broadcast"
                    url = "https://github.com/androidbroadcast"
                }

                scm {
                    url = "https://github.com/androidbroadcast/NavState"
                }

                issueManagement {
                    url = "https://github.com/androidbroadcast/NavState/issues"
                }

                ciManagement {
                    url = "https://github.com/androidbroadcast/NavState/actions"
                }
            }
        }

    repositories {
        maven(uri(rootProject.layout.buildDirectory.dir("maven-repo"))) {
            name = "BuildDir"
        }
    }
}

