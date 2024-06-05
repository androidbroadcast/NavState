package dev.androidbroadcast.navstate.sample.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.androidbroadcast.navstate.Forward
import dev.androidbroadcast.navstate.LocalNavigator

@Composable
fun Ping(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Text(
                text = "Ping",
                modifier = modifier,
            )

            Spacer(modifier = Modifier.height(8.dp))

            val navigator = checkNotNull(LocalNavigator.current)
            Button(onClick = { navigator.enqueue(Forward(PingPongNavGraph.Pong())) }) {
                Text(text = "Next")
            }
        }
    }
}

@Composable
fun Pong(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Text(
                text = "Pong",
                modifier = modifier,
            )

            Spacer(modifier = Modifier.height(8.dp))

            val navigator = checkNotNull(LocalNavigator.current)
            Button(onClick = { navigator.enqueue(Forward(PingPongNavGraph.Ping())) }) {
                Text(text = "Next")
            }
        }
    }
}
