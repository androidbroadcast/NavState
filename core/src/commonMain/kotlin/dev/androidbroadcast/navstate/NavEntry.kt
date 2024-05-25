package dev.androidbroadcast.navstate

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
public data class NavEntry(
    val destination: @Contextual NavDest,
    val tags: List<@Contextual Any> = emptyList()
) {

    public constructor(
        destination: NavDest,
        tag: Any,
    ) : this(destination, listOf(tag))
}