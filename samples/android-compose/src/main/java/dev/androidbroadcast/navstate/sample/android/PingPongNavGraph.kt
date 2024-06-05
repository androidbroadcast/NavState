package dev.androidbroadcast.navstate.sample.android

import dev.androidbroadcast.navstate.NavDest
import kotlinx.serialization.Serializable

sealed interface PingPongNavGraph : NavDest {
    @Serializable
    class Ping : PingPongNavGraph

    @Serializable
    class Pong : PingPongNavGraph

    companion object {

        val root = Ping()
    }
}
