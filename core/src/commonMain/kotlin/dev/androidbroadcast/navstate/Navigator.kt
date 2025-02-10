package dev.androidbroadcast.navstate

import dev.androidbroadcast.navstate.deeplink.UriMatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive

public class Navigator(
    initialState: NavState
) {

    init {
        validate(initialState)
    }

    private val deepLinks = mutableMapOf<UriMatcher, DeepLinkHandler>()

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

    public fun enqueue(command: NavCommand) {
        updateState(command.transform(currentState))
    }

    public fun interface DeepLinkHandler {

        public fun handle(navigator: Navigator, uri: String, result: UriMatcher.MatchResult): Boolean
    }

    public interface StateTransformDeepLinkHandler : DeepLinkHandler {

        public override fun handle(navigator: Navigator, uri: String, result: UriMatcher.MatchResult): Boolean {
            navigator.replaceState { curState -> transformState(curState) }
            return true
        }

        public fun transformState(curState: NavState): NavState
    }
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
    check(navState.structures.isNotEmpty()) { "NavState must have at least 1 struct" }
    check(navState.current in navState.structures) { "Active struct out of structs" }
}

public inline fun Navigator.replaceState(
    crossinline transform: (curState: NavState) -> NavState,
) {
    enqueue { curState -> transform(curState) }
}
