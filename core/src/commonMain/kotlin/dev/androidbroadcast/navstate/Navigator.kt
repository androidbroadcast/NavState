package dev.androidbroadcast.navstate

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

public class Navigator(
    initialState: NavState = NavState(emptyList())
) {
    private val commandsQueue = NavCommandsQueue(this)
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

private data class NavCommandEntry(
    val command: NavCommand,
    val modifier: NavModifier,
)

private class NavCommandsQueue(
    private val navigator: Navigator,
    private val commandsScope: CoroutineScope =
        // Dispatchers.Main is right. Don't replace with Dispatchers.Main.immediate
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
) {

    private val queue =
        MutableSharedFlow<NavCommandEntry>(extraBufferCapacity = Int.MAX_VALUE)

    init {
        queue.map { (command, modifier) -> navigator.execute(modifier, command) }
            .launchIn(commandsScope)
    }

    val isCancelled: Boolean
        get() = !commandsScope.isActive

    fun stop() {
        commandsScope.cancel()
    }

    fun enqueue(command: NavCommand, modifier: NavModifier = NavModifier) {
        queue.tryEmit(NavCommandEntry(command, modifier))
    }
}