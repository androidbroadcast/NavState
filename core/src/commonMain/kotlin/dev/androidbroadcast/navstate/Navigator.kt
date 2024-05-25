package dev.androidbroadcast.navstate

import kotlinx.coroutines.flow.*

public class Navigator(
    initialState: NavState = NavState(emptyList())
) {
    private val commandsQueue = DefaultNavCommandsQueue(this)
    private val _stateFlow = MutableStateFlow(initialState)

    public val stateFlow: StateFlow<NavState>
        get() = _stateFlow.asStateFlow()

    public val currentState: NavState
        get() = _stateFlow.value

    public fun enqueue(modifier: NavModifier = NavModifier, command: NavCommand) {
        commandsQueue.enqueue(command, modifier)
    }

    internal fun execute(modifier: NavModifier = NavModifier, command: NavCommand) {
        _stateFlow.value = currentState.let(modifier::beforeCommand)
            .let(command::execute)
            .let(modifier::afterCommand)
    }
}

