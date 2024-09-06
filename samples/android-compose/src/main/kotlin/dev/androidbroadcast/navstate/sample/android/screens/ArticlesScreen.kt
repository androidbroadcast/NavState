package dev.androidbroadcast.navstate.sample.android.screens

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.androidbroadcast.navstate.LocalNavigator
import dev.androidbroadcast.navstate.annotations.NavDest
import dev.androidbroadcast.navstate.sample.android.BroadcastNavGraph
import dev.androidbroadcast.navstate.sample.android.BroadcastNavGraph.Articles

@[Composable NavDest(Articles::class)]
fun ArticlesScreen() {
    val scrollState = rememberScrollState()
    Column(Modifier.scrollable(scrollState, Orientation.Vertical)) {
        val navigator = LocalNavigator.current
        repeat(3) { itemId ->
            Button(
                onClick = {
                    navigator.open("broadcast://androidbroadcast.dev/article/$itemId")
                },
            ) {
                Text(
                    "Article $itemId",
                    Modifier.padding(vertical = 8.dp),
                )
            }
        }
    }
}
