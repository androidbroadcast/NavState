package dev.androidbroadcast.navstate

import dev.androidbroadcast.navstate.deeplink.UriMatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

public class Navigator(
    initialState: NavState,
) {

    private val deepLinks = mutableMapOf<UriMatcher, DeepLinkHandler>()
    public val commandsQueue: NavCommandsQueue = DefaultNavCommandsQueue(this)

    private val _stateFlow = MutableStateFlow(initialState)

    public val stateFlow: StateFlow<NavState>
        get() = _stateFlow.asStateFlow()

    public val currentState: NavState
        get() = _stateFlow.value

    public fun registerDeepLink(matcher: UriMatcher, handler: DeepLinkHandler) {
        deepLinks[matcher] = handler
    }

    public fun open(uri: String): Boolean {
        return deepLinks.any { (pattern, handler) ->
            val result = pattern.match(uri) ?: return@any false
            handler.handle(this@Navigator, uri, result)
        }
    }

    internal fun updateState(state: NavState) {
        _stateFlow.value = state.also(::validate)
    }

    public fun interface DeepLinkHandler {

        public fun handle(navigator: Navigator, uri: String, result: UriMatcher.MatchResult): Boolean {
            navigator.replaceState { curState -> transformState(curState) }
            return true
        }

        public fun transformState(curState: NavState): NavState
    }
}

public fun Navigator.enqueue(command: NavCommand) {
    commandsQueue.enqueue(command)
}

public suspend fun Navigator.execute(command: NavCommand) {
    commandsQueue.await(command)
}

/**
 * Check [NavState] that it's valid and can be represented navigation state
 *
 * Conditions
 * - Has at least 1 stack
 * - Each stack contains at least 1 stack
 * - Active stack must be in NavState stacks
 */
private fun validate(navState: NavState) {
    check(navState.stacks.isNotEmpty()) { "NavState must have at least 1 NavStack" }
    check(navState.activeStack in navState.stacks) { "Active Stack isn't in stacks" }
    navState.stacks.forEach { navStack ->
        check(navStack.entries.isNotEmpty()) {
            "NavStack must have at least 1 NavEntry"
        }
    }
}

public inline fun Navigator.replaceState(
    crossinline transform: (curState: NavState) -> NavState,
) {
    enqueue { curState -> transform(curState) }
}
