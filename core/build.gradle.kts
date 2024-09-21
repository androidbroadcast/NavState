plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
}

group = "dev.androidbroadcast.navstate"
version = "0.1"

android {
    namespace = "dev.androidbroadcast.navstate.core"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        debug {}
        release {}
    }

    publishing {
        singleVariant("release") {
            withSourcesJar() // Required
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("release") {
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            pom {
                name = "NavState"
                description = "KMP Navigation State library"

                ciManagement {
                    system = "GitHub Actions"
                    url = "https://github.com/androidbroadcast/NavState/actions"
                }

                issueManagement {
                    system = "GitHub"
                    url = "https://github.com/androidbroadcast/navstate/issues"
                }

                developers {
                    developer {
                        name = "Kirill Rozov"
                        email = "kirill@androidbroadcast.dev"
                    }
                }
            }

            afterEvaluate {
                from(components["release"])
            }
        }

        repositories {
            mavenCentral()
            mavenLocal()
            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/androidbroadcast/navstate")
                credentials {
                    username = System.getenv("GITHUB_USENAME")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
}
