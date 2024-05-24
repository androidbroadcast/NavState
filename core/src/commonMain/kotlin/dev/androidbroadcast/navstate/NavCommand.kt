package dev.androidbroadcast.navstate

public fun interface NavCommand {

    /**
     * Transform [NavState]
     *
     * @return New navigation state
     */
    public fun execute(state: NavState): NavState
}