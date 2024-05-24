package dev.androidbroadcast.navstate

public fun Navigator.forward(
    dest: NavDest,
    tags: List<Any> = emptyList(),
    modifier: NavModifier = NavModifier
) {
    enqueue(modifier) { state -> state.then(NavEntry(dest, tags)) }
}

public fun Navigator.back(
    modifier: NavModifier = NavModifier
) {
    enqueue(modifier) { state ->
        state.entries.let { entries ->
            entries.subList(0, (entries.size - 1).coerceAtLeast(0))
        }.let(::NavState)

    }
}

public fun Navigator.backTo(
    tag: Any,
    modifier: NavModifier = NavModifier
) {
    enqueue(modifier) { state ->
        val entries = state.entries
        val lastIndex = entries.indexOfLast { tag in it.tags }
        if (lastIndex >= 0) NavState(entries.subList(0, lastIndex + 1)) else state.copy()
    }
}

public fun Navigator.replaceState(
    newState: () -> NavState
) {
    enqueue { _ -> newState() }
}