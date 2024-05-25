package dev.androidbroadcast.navstate

public interface NavModifier {

    public fun afterCommand(state: NavState): NavState = state

    public fun beforeCommand(state: NavState): NavState = state

    public fun then(modifier: NavModifier): NavModifier = when {
        modifier is NavModifiers -> NavModifiers(buildList {
            add(this@NavModifier)
            addAll(modifier.modifiers)
        })

        else -> NavModifiers(listOf(this, modifier))
    }

    public companion object : NavModifier {

        override fun then(modifier: NavModifier): NavModifier = modifier
    }
}

private class NavModifiers(
    val modifiers: List<NavModifier>
) : NavModifier {

    override fun afterCommand(state: NavState): NavState {
        return modifiers.fold(state) { acc, modifier -> modifier.afterCommand(acc) }
    }

    override fun beforeCommand(state: NavState): NavState {
        return modifiers.fold(state) { acc, modifier -> modifier.beforeCommand(acc) }
    }

    override fun then(modifier: NavModifier): NavModifier {
        return NavModifiers(
            modifiers = buildList {
                addAll(modifiers)
                if (modifier is NavModifiers) addAll(modifier.modifiers) else add(modifier)
            }
        )
    }
}