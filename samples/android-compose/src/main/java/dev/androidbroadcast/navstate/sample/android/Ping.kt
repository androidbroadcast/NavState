@file:Suppress("UNUSED_PARAMETER")

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
import dev.androidbroadcast.navstate.LocalNavigator
import dev.androidbroadcast.navstate.NavCommand
import dev.androidbroadcast.navstate.annotations.NavDest
import dev.androidbroadcast.navstate.forward

@Composable
@NavDest(dest = PingPongNavGraph.Ping::class)
fun Ping(
    dest: PingPongNavGraph.Ping,
    modifier: Modifier = Modifier,
) {
    val navigator = LocalNavigator.current
    Ping(
        onNextClick = { navigator.enqueue(NavCommand.forward(PingPongNavGraph.Pong())) },
        modifier,
    )
}

@Composable
private fun Ping(
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Column {
            Text(
                text = "Ping",
                modifier = modifier,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = onNextClick) {
                Text(text = "Next")
            }
        }
    }
}
