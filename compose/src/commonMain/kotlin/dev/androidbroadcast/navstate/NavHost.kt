package dev.androidbroadcast.navstate

import androidx.compose.runtime.*
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.arkivanov.essenty.backhandler.BackHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map

public val LocalNavigator: ProvidableCompositionLocal<Navigator?> = compositionLocalOf { null }

@Composable
public fun rememberNavState(): State<NavState> {
    val navigator = checkNotNull(LocalNavigator.current)
    return navigator.stateFlow.collectAsState(Dispatchers.Main.immediate)
}

@Composable
public fun rememberNavTopEntry(): State<NavEntry> {
    val navigator = checkNotNull(LocalNavigator.current)
    return navigator.stateFlow
        .map { it.entries.last() }
        .collectAsState(
            initial = navigator.currentState.entries.last(),
            context = Dispatchers.Main.immediate,
        )
}

@Composable
@NonRestartableComposable
public fun NavHost(
    initialDestination: NavDest,
    onRootBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    NavHost(
        initialState = NavState(listOf(NavEntry(initialDestination))),
        onRootBack,
        content,
    )
}

@Composable
internal expect fun platformBackDispatcher(): BackDispatcher

@Composable
@NonRestartableComposable
public fun NavHost(
    initialState: NavState,
    onRootBack: () -> Unit,
    content: @Composable () -> Unit,
) {
    val navigator = Navigator(initialState)

    BackHandler(
        backHandler = platformBackDispatcher(),
        onBack = {
            if (navigator.currentState.entries.size > 1) {
                navigator.back()
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
