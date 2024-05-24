package dev.androidbroadcast.navstate

public data class NavEntry(
    val destination: NavDest,
    val tags: List<Any> = emptyList()
) {

    public constructor(destination: NavDest, tag: Any) : this(destination, listOf(tag))
}