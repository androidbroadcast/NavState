package dev.androidbroadcast.navstate.sample.android.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.androidbroadcast.navstate.LocalNavigator
import dev.androidbroadcast.navstate.NavCommand
import dev.androidbroadcast.navstate.annotations.NavDest
import dev.androidbroadcast.navstate.sample.android.BroadcastNavGraph
import dev.androidbroadcast.navstate.sample.android.BroadcastNavGraph.NavMenu
import dev.androidbroadcast.navstate.stack.forward

@[Composable NavDest(NavMenu::class)]
@Preview
fun NavigationMenuScreen() {
    val navigator = LocalNavigator.current
    Column(Modifier.fillMaxSize()) {
        Row {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1F),
            ) {
                Button(
                    onClick = {
                        navigator.enqueue(
                            NavCommand.forward(
                                BroadcastNavGraph.Articles(),
                            ),
                        )
                    },
                ) {
                    Text("Articles")
                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1F),
            ) {
                Button(
                    onClick = { navigator.enqueue(NavCommand.forward(BroadcastNavGraph.BroadcastResources())) },
                ) {
                    Text("Resources")
                }
            }
        }
        Row {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1F),
            ) {
                Button(
                    onClick = { navigator.enqueue(NavCommand.forward(BroadcastNavGraph.AboutAuthor())) },
                ) {
                    Text("About Author")
                }
            }
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.weight(1F),
            ) {
                Button(
                    onClick = {
                        navigator.enqueue(
                            NavCommand.forward(BroadcastNavGraph.WebPageDest("https://androidbroadcast.dev")),
                        )
                    },
                ) {
                    Text("Site")
                }
            }
        }
    }
}
