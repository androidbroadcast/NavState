package dev.androidbroadcast.navstate

import kotlinx.coroutines.flow.*

public class Navigator(initialState: NavState) {

    private val commandsQueue = DefaultNavCommandsQueue(this)
    private val _stateFlow = MutableStateFlow(initialState)

    public val stateFlow: StateFlow<NavState>
        get() = _stateFlow.asStateFlow()

    public val currentState: NavState
        get() = _stateFlow.value

    public fun enqueue(command: NavCommand) {
        commandsQueue.enqueue(command)
    }

    internal fun execute(command: NavCommand) {
        _stateFlow.value = command.execute(currentState).apply(NavState::validate)
    }
}

