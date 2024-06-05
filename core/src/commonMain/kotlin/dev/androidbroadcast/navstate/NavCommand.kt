package dev.androidbroadcast.navstate

public fun interface NavCommand {

    /**
     * Transform [NavState]
     *
     * @return New navigation state
     */
    public fun execute(state: NavState): NavState

    public fun then(command: NavCommand): NavCommand =
        when {
            command is NavCommandList ->
                NavCommandList(
                    buildList {
                        add(this@NavCommand)
                        addAll(command.commands)
                    },
                )

            else -> NavCommandList(listOf(this, command))
        }
}

private class NavCommandList(
    val commands: List<NavCommand>,
) : NavCommand {

    override fun execute(state: NavState): NavState {
        return commands.fold(state) { accState, command -> command.execute(accState) }
    }

    override fun then(command: NavCommand): NavCommand {
        return NavCommandList(
            commands =
            buildList {
                addAll(commands)
                if (command is NavCommandList) addAll(command.commands) else add(command)
            },
        )
    }
}

public operator fun NavCommand.plus(command: NavCommand): NavCommand = then(command)

public fun NavCommand(vararg commands: NavCommand): NavCommand {
    require(commands.isNotEmpty())
    return when (commands.size) {
        1 -> commands[0]
        else -> NavCommandList(commands.toList())
    }
}
