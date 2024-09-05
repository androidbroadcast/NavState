package dev.androidbroadcast.navstate.sample.android.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.androidbroadcast.navstate.annotations.NavDest
import dev.androidbroadcast.navstate.sample.android.BroadcastNavGraph

@Composable
@NavDest(BroadcastNavGraph.AboutAuthor::class)
fun AboutAuthorScreen() {
    Text("About")
}
