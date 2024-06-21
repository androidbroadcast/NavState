import dev.androidbroadcast.navstate.buildlogic.configMavenPublish
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure

class MavenPublishConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("maven-publish")
            }

            extensions.configure<PublishingExtension> {
                configMavenPublish(
                    this,
                )
            }
        }
    }
}
