package dev.androidbroadcast.navstate.sample.android

import dev.androidbroadcast.navstate.NavDest
import kotlinx.serialization.Serializable

sealed interface PingPongDest: NavDest {

    @Serializable
    class Ping : PingPongDest

    @Serializable
    class Pong : PingPongDest
}