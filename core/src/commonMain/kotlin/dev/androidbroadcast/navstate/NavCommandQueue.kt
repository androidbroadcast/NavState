package dev.androidbroadcast.navstate

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive

public interface NavCommandsQueue {

    public val isCancelled: Boolean

    public fun enqueue(command: NavCommand)

    public suspend fun await(command: NavCommand)
}

internal class DefaultNavCommandsQueue(
    private val navigator: Navigator,
    private val commandsScope: CoroutineScope =
        // Dispatchers.Main is right. Don't replace with Dispatchers.Main.immediate
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
) : NavCommandsQueue {

    private val queue = MutableSharedFlow<NavCommand>(extraBufferCapacity = Int.MAX_VALUE)

    init {
        queue.map { it.execute(navigator.currentState) }
            .onEach(navigator::updateState)
            .launchIn(commandsScope)
    }

    override val isCancelled: Boolean
        get() = !commandsScope.isActive

    override fun enqueue(command: NavCommand) {
        queue.tryEmit(command)
    }

    override suspend fun await(command: NavCommand) {
        queue.emit(command)
    }
}
