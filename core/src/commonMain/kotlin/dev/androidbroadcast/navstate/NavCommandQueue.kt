package dev.androidbroadcast.navstate

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.isActive

internal interface NavCommandsQueue {

    val isCancelled: Boolean

    fun stop()

    fun enqueue(command: NavCommand, modifier: NavModifier = NavModifier)
}

internal class DefaultNavCommandsQueue(
    private val navigator: Navigator,
    private val commandsScope: CoroutineScope =
        // Dispatchers.Main is right. Don't replace with Dispatchers.Main.immediate
        CoroutineScope(SupervisorJob() + Dispatchers.Main)
): NavCommandsQueue {

    private val queue =
        MutableSharedFlow<NavCommandEntry>(extraBufferCapacity = Int.MAX_VALUE)

    init {
        queue.map { (command, modifier) -> navigator.execute(modifier, command) }
            .launchIn(commandsScope)
    }

    override val isCancelled: Boolean
        get() = !commandsScope.isActive

    override fun stop() {
        commandsScope.cancel()
    }

    override fun enqueue(command: NavCommand, modifier: NavModifier) {
        queue.tryEmit(NavCommandEntry(command, modifier))
    }
}

internal data class NavCommandEntry(
    val command: NavCommand,
    val modifier: NavModifier,
)