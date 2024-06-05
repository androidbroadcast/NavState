package dev.androidbroadcast.navstate.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import dev.androidbroadcast.navstate.NavHost
import dev.androidbroadcast.navstate.rememberNavTopEntry
import dev.androidbroadcast.navstate.sample.android.ui.theme.PingPongTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PingPongTheme {
                NavHost(
                    initialDestination = PingPongNavGraph.root,
                    onRootBack = this@MainActivity::finish,
                ) {
                    val topEntry by rememberNavTopEntry()
                    when (val dest = topEntry.destination) {
                        is PingPongNavGraph.Ping -> Ping()
                        is PingPongNavGraph.Pong -> Pong()
                        else -> error("Unhandled destination = $dest")
                    }
                }
            }
        }
    }
}
