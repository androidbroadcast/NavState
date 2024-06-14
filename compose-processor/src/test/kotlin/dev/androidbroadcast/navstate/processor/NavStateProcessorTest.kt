@file:OptIn(ExperimentalCompilerApi::class)

package dev.androidbroadcast.navstate.processor

import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.SourceFile
import com.tschuchort.compiletesting.symbolProcessorProviders
import org.jetbrains.kotlin.compiler.plugin.ExperimentalCompilerApi
import kotlin.test.Test
import kotlin.test.assertEquals

class NavStateProcessorTest {

    @Test
    fun `test processor`() {
        val kotlinSource =
            SourceFile.kotlin(
                name = "NavDestTest.kt",
                contents = """
            package dev.androidbroadcast.test.sample

            import dev.androidbroadcast.navstate.annotations.NavDest
            import androidx.compose.ui.Modifier
            import androidx.compose.runtime.Composable
            import kotlinx.serialization.Serializable

            @Serializable
            class PingDest
        
            @Serializable
            class PongDest
    
            @Composable
            @NavDest(dest = PongDest::class)
            fun Pong(
                dest: PongDest,
            ) {}
    
            @Composable
            @NavDest(dest = PingDest::class)
            fun Ping(
                modifier: Modifier = Modifier,
            ) {}
        """,
            )

        val result =
            KotlinCompilation()
                .apply {
                    sources = listOf(kotlinSource)

                    symbolProcessorProviders = listOf(NavStateProcessorProvider())

                    multiplatform = true
                    inheritClassPath = true
                    messageOutputStream = System.out // see diagnostics in real time
                }.compile()

        assertEquals(KotlinCompilation.ExitCode.OK, result.exitCode)
    }
}
