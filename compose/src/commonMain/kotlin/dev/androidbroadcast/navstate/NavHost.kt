package dev.androidbroadcast.navstate

import androidx.compose.runtime.*

public val LocalNavigator: ProvidableCompositionLocal<Navigator> =
    staticCompositionLocalOf { error("No Navigator in composition was specified") }

@Composable
@NonRestartableComposable
public fun NavHost(initialDestination: NavDest, content: @Composable () -> Unit) {
    NavHost(NavState(listOf(NavEntry(initialDestination))), content)
}


@Composable
@NonRestartableComposable
public fun NavHost(initialState: NavState, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalNavigator provides Navigator(initialState),
        content
    )
}