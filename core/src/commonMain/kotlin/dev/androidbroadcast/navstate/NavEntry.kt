package dev.androidbroadcast.navstate

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
public data class NavEntry(
    val destination: @Contextual NavDest,
    val tags: List<@Contextual Any> = emptyList(),
)

public fun NavEntry(
    destination: NavDest,
    tag: Any,
): NavEntry = NavEntry(destination, listOf(tag))
