package dev.androidbroadcast.navstate.buildlogic

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.maven

internal fun Project.configMavenPublish(
    publishingExt: PublishingExtension,
): Unit =
    with(publishingExt) {
        publications {
            create("maven", MavenPublication::class.java) {
                groupId = "dev.androidbroadcast.navstate"
                artifactId = this@configMavenPublish.name
                version = this@configMavenPublish.version.toString()

                pom {
                    name = this@configMavenPublish.displayName
                    description = this@configMavenPublish.description
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
        }

        repositories {
            mavenLocal()

            maven("https://jitpack.io") {
                name = "Jitpack"
            }

            // Publish to https://github.com/androidbroadcast/NavState
            maven("https://maven.pkg.github.com/androidbroadcast/NavState") {
                name = "GitHubPackages"
                credentials {
                    username = System.getenv("GITHUB_ACTOR")
                    password = System.getenv("GITHUB_TOKEN")
                }
            }
        }
    }
