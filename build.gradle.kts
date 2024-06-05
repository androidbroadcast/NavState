import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlin.serialization).apply(false)
    alias(libs.plugins.kotlin.android).apply(false)
    alias(libs.plugins.compose.compiler).apply(false)
    alias(libs.plugins.compose).apply(false)
    alias(libs.plugins.detekt).apply(false)
}

allprojects {
    afterEvaluate {
        plugins.apply(libs.plugins.detekt.get().pluginId)

        extensions.configure(DetektExtension::class) {
            config.setFrom(rootProject.file("config/detekt/detekt.yml"))
        }
    }
}
