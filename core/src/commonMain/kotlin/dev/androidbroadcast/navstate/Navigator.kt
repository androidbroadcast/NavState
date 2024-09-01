package dev.androidbroadcast.navstate

import dev.androidbroadcast.navstate.deeplink.DeepLinkProcessor
import dev.androidbroadcast.navstate.deeplink.SimpleDeepLinkProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public class Navigator(
    initialState: NavState,
) : DeepLinkProcessor by SimpleDeepLinkProcessor() {


    public val commandsQueue: NavCommandsQueue = DefaultNavCommandsQueue(this)

    private val _stateFlow = MutableStateFlow(initialState)

    public val stateFlow: StateFlow<NavState>
        get() = _stateFlow.asStateFlow()

    public val currentState: NavState
        get() = _stateFlow.value

    internal fun updateState(state: NavState) {
        _stateFlow.value = state.also(::validate)
    }
}

public fun Navigator.enqueue(command: NavCommand) {
    commandsQueue.enqueue(command)
}

private fun validate(navState: NavState) {
    check(navState.stacks.isNotEmpty()) { "NavState must have at least 1 NavStack" }
    check(navState.activeStack in navState.stacks) { "Active Stack isn't in stacks" }
    navState.stacks.forEach { navStack ->
        check(navStack.entries.isNotEmpty()) {
            "NavStack must have at least 1 NavEntry"
        }
    }
}

public inline fun Navigator.replaceState(crossinline newState: () -> NavState) {
    enqueue { newState() }
}
