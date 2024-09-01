package dev.androidbroadcast.navstate.sample.android

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.androidbroadcast.navstate.NavCommand
import dev.androidbroadcast.navstate.NavState
import dev.androidbroadcast.navstate.Navigator
import dev.androidbroadcast.navstate.annotations.NavDest
import dev.androidbroadcast.navstate.enqueue
import dev.androidbroadcast.navstate.forward
import dev.androidbroadcast.navstate.popTop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.coroutines.EmptyCoroutineContext
import dev.androidbroadcast.navstate.NavDest as INavDest


class LauncherDest : INavDest
class HomeDest : INavDest
data class WebPageDest(val url: String) : INavDest

@Composable
fun SampleNavHost(
    initialDestination: INavDest,
    onRootBack: () -> Unit,
    initialStackId: String = NavState.DefaultStackId,
) {
    GeneratedNavHost(initialDestination, onRootBack)
}

@[Composable NavDest(LauncherDest::class)]
fun LauncherScreen(modifier: Modifier = Modifier) {

}

@[Composable NavDest(HomeDest::class)]
fun HomeScreen(modifier: Modifier = Modifier) {

}

@[Composable NavDest(WebPageDest::class)]
fun WebPageScreen(dest: WebPageDest, modifier: Modifier = Modifier) {
}

private fun sample(navigator: Navigator) {
    navigator.enqueue(
        NavCommand.popTop(count = 1)
            .forward(HomeDest()),
    )

    val coroutineScope = CoroutineScope(EmptyCoroutineContext)

    navigator.stateFlow
        .onEach { state ->
            // Показываем UI, соответсвующий текущему состоянию навигации
        }
        .launchIn(coroutineScope)
}
