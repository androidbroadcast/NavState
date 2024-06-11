package dev.androidbroadcast.navstate

public fun NavCommand.forward(
    dest: NavDest,
    tags: List<Any> = emptyList(),
): NavCommand = then(
    NavCommand { state ->
        state.buildNavState {
            add(state.activeStack.then(NavEntry(dest, tags)))
        }
    },
)

@Deprecated(
    "Use PopTop() instead",
    ReplaceWith("popTop(count = 1)"),
)
public fun NavCommand.back(): NavCommand = popTop(count = 1)

public inline fun Navigator.replaceState(crossinline newState: () -> NavState): NavCommand {
    return NavCommand { newState() }
}

public fun NavCommand.popTop(
    count: Int = 1,
): NavCommand {
    require(count > 0) { "Count must be positive value" }
    return then(
        NavCommand { state ->
            state.buildNavState {
                add(
                    state.activeStack.let { stack ->
                        val entries = stack.entries.toMutableList()
                        stack.copy(
                            entries = entries.subList(0, (entries.size - count).coerceAtLeast(1)),
                        )
                    }
                )
            }
        },
    )
}

public fun NavCommand.switchStack(newActiveStackId: String): NavCommand = then(
    NavCommand { state ->
        val newActiveStack = state.stacks.firstOrNull { it.id == newActiveStackId }
        if (newActiveStack != null) state.copy(activeStack = newActiveStack) else state
    },
)
