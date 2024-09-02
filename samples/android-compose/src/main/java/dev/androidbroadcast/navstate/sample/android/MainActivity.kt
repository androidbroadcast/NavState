package dev.androidbroadcast.navstate.sample.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.androidbroadcast.navstate.NavState
import dev.androidbroadcast.navstate.Navigator
import dev.androidbroadcast.navstate.sample.android.ui.theme.PingPongTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PingPongTheme {
                val navigator = Navigator(NavState(PingPongNavGraph.root))
                GeneratedNavHost(
                    navigator,
                    onRootBack = this@MainActivity::finish,
                )
            }
        }
    }
}
