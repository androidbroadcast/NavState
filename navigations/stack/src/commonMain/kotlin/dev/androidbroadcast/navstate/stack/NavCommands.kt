package dev.androidbroadcast.navstate.stack

import dev.androidbroadcast.navstate.NavCommand
import dev.androidbroadcast.navstate.NavDest
import dev.androidbroadcast.navstate.NavEntry
import dev.androidbroadcast.navstate.NavStructure
import dev.androidbroadcast.navstate.buildNavState

public fun NavCommand.forward(
    dest: NavDest,
    tags: List<Any> = emptyList(),
): NavCommand = then { state ->
    val currentStruct = state.current as NavStack
    state.buildNavState {
        add(currentStruct.copy(entries = currentStruct.entries + NavEntry(dest, tags)))
    }
}

public fun NavCommand.forward(
    dest: NavDest,
    stackId: NavStructure.Id,
    tags: List<Any> = emptyList(),
): NavCommand = then { state ->
    val stack = state.structures.firstOrNull { it.id == stackId && it is NavStack } as NavStack?
    val newEntry = NavEntry(dest, tags.toList())
    state.buildNavState {
        val newActiveStack =
            stack?.copy(entries = stack.entries + newEntry) ?: NavStack(id = stackId, listOf(newEntry))
        add(newActiveStack)
    }
}

@Deprecated(
    "Use PopTop() instead",
    ReplaceWith("popTop()"),
)
public fun NavCommand.back(): NavCommand = popTop(count = 1)

public fun NavCommand.popTop(entryTag: Any): NavCommand = then { state ->
    val stack = state.current as NavStack
    val entryIndex = stack.entries.indexOfLast { entryTag in it.tags }
    if (entryIndex < 0) {
        state
    } else {
        state.buildNavState {
            add(stack.copy(entries = stack.entries.subList(0, entryIndex + 1)))
        }
    }
}

public fun NavCommand.popTop(
    count: Int = 1,
): NavCommand {
    require(count > 0) { "Count must be positive value" }
    return then { state ->
        state.buildNavState {
            add(
                (state.current as NavStack)
                    .let { entriesContainer ->
                        val entries = entriesContainer.entries
                        val newEntries = entries.subList(0, (entries.size - count).coerceAtLeast(1))
                        entriesContainer.copy(entries = newEntries)
                    },
            )
        }
    }
}
