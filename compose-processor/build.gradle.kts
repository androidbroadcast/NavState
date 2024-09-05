plugins {
    id("java-library")
    alias(libs.plugins.kotlin.jvm)
//    id("dev.androidbroadcast.navstate.mavenPublish")
}

group = "dev.androidbroadcast.navstate"
version = "0.1"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21

    withSourcesJar()
}

dependencies {
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet.ksp)

    testImplementation(libs.ksp.testing)
    testImplementation(libs.kotlin.test)
    testImplementation(projects.navstateCompose)
    testImplementation(projects.navstateComposeAnnotations)
}

//publishing {
//    publications.getByName<MavenPublication>("maven") {
//        version = "0.1"
//        from(components["java"])
//    }
//}
