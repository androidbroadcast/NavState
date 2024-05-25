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
                    initialDestination = PingPongDest.Ping(),
                    onRootBack = { finish() }
                ) {
                    val topEntry by rememberNavTopEntry()
                    when (val dest = topEntry.destination) {
                        is PingPongDest.Ping -> Ping()
                        is PingPongDest.Pong -> Pong()
                        else -> error("Unhandled destination = $dest")
                    }
                }
            }
        }
    }
}

