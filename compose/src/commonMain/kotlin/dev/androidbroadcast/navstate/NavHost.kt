package dev.androidbroadcast.navstate

import androidx.compose.runtime.*
import kotlinx.coroutines.Dispatchers

public val LocalNavigator: ProvidableCompositionLocal<Navigator> =
    staticCompositionLocalOf { error("No Navigator in composition was specified") }

@Composable
public fun rememberNavState(): State<NavState> {
    val navigator = LocalNavigator.current
    return remember { navigator.stateFlow.collectAsState(Dispatchers.Main.immediate) }
}

@Composable
@NonRestartableComposable
public fun NavHost(
    initialDestination: NavDest,
    content: @Composable () -> Unit,
) {
    NavHost(
        initialState = NavState(listOf(NavEntry(initialDestination))),
        content,
    )
}


@Composable
@NonRestartableComposable
public fun NavHost(
    initialState: NavState,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalNavigator provides Navigator(initialState),
        content,
    )
}