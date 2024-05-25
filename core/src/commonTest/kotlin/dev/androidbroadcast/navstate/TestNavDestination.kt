package dev.androidbroadcast.navstate

import kotlinx.serialization.Serializable

sealed interface TestDestinations: NavDest {

    @Serializable
    data object Root : TestDestinations

    @Serializable
    data object  DataList : TestDestinations

    @Serializable
    data class Details(val dataId: String) : TestDestinations
}

