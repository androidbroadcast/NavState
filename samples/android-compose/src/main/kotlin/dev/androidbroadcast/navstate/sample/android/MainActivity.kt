package dev.androidbroadcast.navstate.sample.android

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dev.androidbroadcast.navstate.sample.android.screens.RootScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RootScreen(
                onRootBack = this@MainActivity::finish,
                openWebPage = ::openWebPage,
            )
        }
    }

    private fun openWebPage(url: String): Boolean {
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(url)),
            )
        } catch (e: ActivityNotFoundException) {
            // Can't view url
            return false
        }
        return true
    }
}
