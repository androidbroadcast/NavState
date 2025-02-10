package dev.androidbroadcast.navstate

/**
 * Switch between already created task. If task doesn't exist then do nothing
 */
public fun NavCommand.switchCurrent(currentId: NavStructure.Id): NavCommand = then { state ->
    val hasEntriesStruct = state.structures.any { it.id == currentId && it is NavEntriesStructure }
    if (hasEntriesStruct) state.copy(currentId = currentId) else state
}

public fun NavCommand.add(
    structure: NavStructure,
): NavCommand = then { state ->
    state.copy(
        structures = state.structures + structure,
        currentId = structure.id,
    )
}

public fun NavCommand.add(
    structure: NavStructure,
    setCurrent: Boolean,
): NavCommand {
    if (setCurrent) {
        return then { state ->
            state.copy(
                structures = state.structures + structure,
                currentId = structure.id,
            )
        }
    } else {
        return add(structure)
    }
}
