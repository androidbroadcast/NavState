package dev.androidbroadcast.navstate

public fun NavModifier.popTop(count: Int = 1): NavModifier = then(PopTop(count))

private class PopTop(
    private val count: Int = 1
) : NavModifier {

    init {
        require(count > 0) { "Count must be positive value" }
    }

    override fun beforeCommand(state: NavState): NavState {
        val entries = state.entries.toMutableList()
        return NavState(
            entries.subList(0, (entries.size - count).coerceAtLeast(0))
        )
    }
}

public fun NavModifier.clearState(): NavModifier = then(ClearState())

private class ClearState : NavModifier {

    override fun beforeCommand(state: NavState): NavState {
        return NavState(entries = emptyList())
    }

    override fun toString() = "ClearState"
}