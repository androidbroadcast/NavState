plugins {
    alias(libs.plugins.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

dependencies {
    implementation(libs.ksp.api)
    implementation(libs.kotlinpoet.ksp)

    testImplementation(libs.ksp.testing)
    testImplementation(libs.kotlin.test)
    testImplementation(projects.compose)
    testImplementation(projects.composeAnnotations)
}
