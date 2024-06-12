package dev.androidbroadcast.navstate

import kotlinx.coroutines.flow.*

public class Navigator(
    initialState: NavState,
) {
    private val commandsQueue: NavCommandsQueue = DefaultNavCommandsQueue(this)

    private val _stateFlow = MutableStateFlow(initialState)

    public val stateFlow: StateFlow<NavState>
        get() = _stateFlow.asStateFlow()

    public val currentState: NavState
        get() = _stateFlow.value

    public fun enqueue(command: NavCommand) {
        commandsQueue.enqueue(command)
    }

    internal fun updateState(state: NavState) {
        _stateFlow.value = state.apply(NavState::validate)
    }
}

public inline fun Navigator.replaceState(crossinline newState: () -> NavState) {
    enqueue(NavCommand { newState() })
}
