package dev.androidbroadcast.navstate

public class Forward(
    private val dest: NavDest,
    private val tags: List<Any> = emptyList(),
) : NavCommand {
    override fun execute(state: NavState): NavState {
        return state.then(NavEntry(dest, tags))
    }
}

@Suppress("ktlint:standard:function-naming")
@Deprecated(
    "Use PopTop() instead",
    ReplaceWith("PopTop()", "dev.androidbroadcast.navstate.PopTop"),
)
public fun Back(): NavCommand = PopTop()

public class BackTo(
    private val tag: Any,
) : NavCommand {
    override fun execute(state: NavState): NavState {
        val entries = state.entries
        val lastIndex = entries.indexOfLast { tag in it.tags }
        return if (lastIndex >= 0) NavState(entries.subList(0, lastIndex + 1)) else state.copy()
    }
}

public inline fun Navigator.replaceState(crossinline newState: () -> NavState) {
    enqueue { _ -> newState() }
}

public class PopTop(
    private val count: Int = 1,
) : NavCommand {
    init {
        require(count > 0) { "Count must be positive value" }
    }

    override fun execute(state: NavState): NavState {
        val entries = state.entries.toMutableList()
        return NavState(
            entries.subList(0, (entries.size - count).coerceAtLeast(0)),
        )
    }

    override fun toString(): String = "PopTop(count=$count)"
}

private class ClearState : NavCommand {
    override fun execute(state: NavState): NavState {
        return NavState(entries = emptyList())
    }

    override fun toString() = "ClearState"
}
