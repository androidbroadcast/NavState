package dev.androidbroadcast.navstate

import kotlinx.serialization.Serializable

@Serializable
public data class NavState(
    val entries: List<NavEntry>
): List<NavEntry> by entries

public fun NavState.then(entry: NavEntry): NavState = NavState(entries + entry)
