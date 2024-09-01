package dev.androidbroadcast.navstate

private data class ForwardInSameStack(
    private val dest: NavDest,
    private val tags: List<Any>,
) : NavCommand {

    override fun execute(state: NavState): NavState {
        return state.buildNavState {
            add(state.activeStack.then(NavEntry(dest, tags)))
        }
    }
}

public fun NavCommand.forward(
    dest: NavDest,
    tags: List<Any> = emptyList(),
): NavCommand = then(ForwardInSameStack(dest, tags))

private data class Forward(
    private val dest: NavDest,
    private val stackId: String,
    private val tags: List<Any>,
) : NavCommand {

    override fun execute(state: NavState): NavState {
        val stack = state.stacks.firstOrNull { it.id == stackId }
        val newEntry = NavEntry(dest, tags.toList())
        return state.buildNavState {
            val newActiveStack =
                stack?.copy(entries = stack.entries + newEntry) ?: NavStack(id = stackId, listOf(newEntry))
            add(newActiveStack, makeActive = true)
        }
    }
}

public fun NavCommand.forward(
    dest: NavDest,
    stackId: String,
    tags: List<Any> = emptyList(),
): NavCommand = then(Forward(dest, stackId, tags))

@Deprecated(
    "Use PopTop() instead",
    ReplaceWith("popTop(count = 1)"),
)
public fun NavCommand.back(): NavCommand = popTop(count = 1)

private data class PopTop(
    private val entryTag: Any,
): NavCommand{

    override fun execute(state: NavState): NavState {
        return state.activeStack.let { stack ->
            val entryIndex = stack.entries.indexOfLast { entryTag in it.tags }
            if (entryIndex < 0) {
                state
            } else {
                state.buildNavState {
                    add(stack.copy(entries = stack.entries.subList(0, entryIndex + 1)))
                }
            }
        }
    }
}

public fun NavCommand.popTop(entryTag: Any): NavCommand = then(PopTop(entryTag))

public fun NavCommand.popTop(
    count: Int = 1,
): NavCommand {
    require(count > 0) { "Count must be positive value" }
    return then { state ->
        state.buildNavState {
            add(
                state.activeStack.let { stack ->
                    val entries = stack.entries
                    stack.copy(
                        entries = entries.subList(0, (entries.size - count).coerceAtLeast(1)),
                    )
                },
            )
        }
    }
}

/**
 * Switch between already created task. If task doesn't exist then do nothing
 */
public fun NavCommand.switchStack(newActiveStackId: String): NavCommand = then { state ->
    val newActiveStack = state.stacks.firstOrNull { it.id == newActiveStackId }
    if (newActiveStack != null) state.copy(activeStack = newActiveStack) else state
}
