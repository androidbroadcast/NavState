package dev.androidbroadcast.navstate

public fun interface NavCommand {

    /**
     * Transform [NavState]
     *
     * @return New navigation state
     */
    public fun transform(state: NavState): NavState

    /**
     * Combine this [NavCommand] with another [NavCommand] into one [NavCommand].
     */
    public fun then(command: NavCommand): NavCommand =
        NavCommandList(
            when {
                command is NavCommandList ->
                    buildList(1 + command.commands.size) {
                        add(this@NavCommand)
                        addAll(command.commands)
                    }

                else -> listOf(this, command)
            }
        )

    public companion object : NavCommand {

        override fun transform(state: NavState): NavState = state

        override fun then(command: NavCommand): NavCommand = command
    }
}

private class NavCommandList(
    val commands: List<NavCommand>,
) : NavCommand {

    override fun transform(state: NavState): NavState {
        return commands.fold(state) { accState, command -> command.transform(accState) }
    }

    override fun then(command: NavCommand): NavCommand {
        return NavCommandList(
            when (command) {
                is NavCommandList -> commands + command.commands
                else -> commands + command
            },
        )
    }
}

public operator fun NavCommand.plus(command: NavCommand): NavCommand = then(command)

public fun NavCommand(vararg commands: NavCommand): NavCommand = when (commands.size) {
    0 -> NavCommand
    1 -> commands[0]
    else -> NavCommandList(commands.toList())
}
