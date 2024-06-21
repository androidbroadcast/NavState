plugins {
    `kotlin-dsl`
}

group = "dev.androidbroadcast.navstate.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    jvmToolchain {
        version = JavaVersion.VERSION_21
    }

    compilerOptions {
        target {
            version = JavaVersion.VERSION_21
        }
    }
}

dependencies {
    compileOnly(libs.gradlePlugins.android)
    compileOnly(libs.gradlePlugins.kotlin)
}

gradlePlugin {
    plugins {
        register("mavenPublish") {
            id = "dev.androidbroadcast.navstate.mavenPublish"
            implementationClass = "MavenPublishConventionPlugin"
        }
    }
}
