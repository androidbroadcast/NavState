package dev.androidbroadcast.navstate

public fun NavCommand.forward(
    dest: NavDest,
    tags: List<Any> = emptyList(),
): NavCommand = then(
    NavCommand { state ->
        state.then(NavEntry(dest, tags))
    },
)

@Deprecated(
    "Use PopTop() instead",
    ReplaceWith("popTop(count = 1)")
)
public fun NavCommand.back(): NavCommand = popTop(count = 1)

public fun NavCommand.backTo(
    tag: Any,
): NavCommand = then { state ->
    val entries = state.entries
    val lastIndex = entries.indexOfLast { tag in it.tags }
    if (lastIndex >= 0) NavState(entries.subList(0, lastIndex + 1)) else state.copy()
}

public inline fun Navigator.replaceState(crossinline newState: () -> NavState) {
    enqueue { _ -> newState() }
}

public fun NavCommand.popTop(
    count: Int = 1,
): NavCommand {
    require(count > 0) { "Count must be positive value" }
    return then(
        NavCommand { state ->
            val entries = state.entries.toMutableList()
            NavState(entries.subList(0, (entries.size - count).coerceAtLeast(0)))
        },
    )
}

public fun NavCommand.clearState(): NavCommand {
    return then(
        NavCommand { NavState(entries = emptyList()) },
    )
}
