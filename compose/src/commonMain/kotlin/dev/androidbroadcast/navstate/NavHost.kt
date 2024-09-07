package dev.androidbroadcast.navstate

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.backhandler.BackHandler
import kotlinx.coroutines.Dispatchers

public val LocalNavigator: ProvidableCompositionLocal<Navigator> =
    compositionLocalOf { error("LocalNavigator must be called inside NavHost()") }

@Composable
public fun rememberNavState(): NavState {
    val navigator = LocalNavigator.current
    val state = navigator.stateFlow.collectAsState(Dispatchers.Main.immediate)
    val remState by remember { state }
    return remState
}

@Composable
public fun rememberNavTopEntry(): NavEntry {
    val navState = rememberNavState()
    return navState.activeStack.entries.last()
}

@Composable
public fun rememberNavTopEntry(count: Int): List<NavEntry> {
    require(count > 0) { "count must be >= 1" }
    val navState = rememberNavState()
    val entries = navState.activeStack.entries
    val startIndex = (entries.size - count).coerceAtLeast(0)
    return entries.subList(startIndex, entries.size)
}

@Composable
@NonRestartableComposable
public fun NavHost(
    initialDestination: NavDest,
    onRootBack: () -> Unit,
    initialStackId: String = NavState.DefaultStackId,
    content: @Composable () -> Unit,
) {
    NavHost(
        initialState = NavState(initialDestination, initialStackId),
        onRootBack,
        content,
    )
}

@Composable
internal expect fun platformBackDispatcher(): BackDispatcher

@Composable
public fun NavHost(
    initialState: NavState,
    onRootBack: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    NavHost(
        navigator = Navigator(initialState),
        onRootBack = onRootBack,
        content = content,
    )
}

@Composable
public fun NavHost(
    navigator: Navigator,
    onRootBack: () -> Unit = {},
    content: @Composable () -> Unit,
) {
    BackHandler(
        backHandler = platformBackDispatcher(),
        onBack = {
            if (navigator.currentState.activeStack.entries.size > 1) {
                navigator.enqueue(NavCommand.popTop(count = 1))
            } else {
                onRootBack()
            }
        },
    )

    CompositionLocalProvider(
        LocalNavigator provides navigator,
        content,
    )
}

@Composable
private fun BackHandler(
    backHandler: BackHandler,
    isEnabled: Boolean = true,
    onBack: () -> Unit,
) {
    val currentOnBack by rememberUpdatedState(onBack)

    val callback =
        remember {
            BackCallback(isEnabled = isEnabled) {
                currentOnBack()
            }
        }

    SideEffect { callback.isEnabled = isEnabled }

    DisposableEffect(backHandler) {
        backHandler.register(callback)
        onDispose { backHandler.unregister(callback) }
    }
}
