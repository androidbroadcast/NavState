import org.jreleaser.model.Active

plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jreleaser)
    signing
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
            maven(url = uri(project.layout.buildDirectory.file("maven-repo"))) {
                name = "BuildDir"
            }
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

jreleaser {
    release {
        project {
            inceptionYear = "2024"
            author("@kirich1409")
        }

        github {
            skipRelease = true
            skipTag = true
        }

        signing {
            active = Active.ALWAYS
            armored = true
            verify = true
        }

        deploy {
            maven {
                mavenCentral.create("sonatype") {
                    active = Active.ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().toString())
                    setAuthorization("Basic")

                    applyMavenCentralRules = false // Wait for fix: https://github.com/kordamp/pomchecker/issues/21
                    sign = true
                    checksums = true
                    sourceJar = true
                    javadocJar = true
                }
            }
        }
    }
}
