package dev.androidbroadcast.navstate

public interface NavModifier {

    public fun afterCommand(state: NavState): NavState = state

    public fun beforeCommand(state: NavState): NavState = state

    public fun then(option: NavModifier): NavModifier = when {
        option is NavOptions -> NavOptions(buildList {
            add(this@NavModifier)
            addAll(option.options)
        })

        else -> NavOptions(listOf(this, option))
    }

    public companion object : NavModifier {

        override fun then(option: NavModifier): NavModifier = option
    }
}

private class NavOptions(
    val options: List<NavModifier>
) : NavModifier {

    override fun afterCommand(state: NavState): NavState {
        return options.fold(state) { acc, option -> option.afterCommand(acc) }
    }

    override fun beforeCommand(state: NavState): NavState {
        return options.fold(state) { acc, option -> option.beforeCommand(acc) }
    }

    override fun then(option: NavModifier): NavModifier {
        return NavOptions(buildList {
            addAll(options)
            if (option is NavOptions) addAll(option.options) else add(option)
        })
    }
}