package dev.androidbroadcast.navstate

import kotlinx.serialization.Serializable

@Serializable
public data class NavStack(
    val id: String,
    val entries: List<NavEntry>
) : Iterable<NavEntry> by entries.reversed()

public fun NavStack(
    id: String,
    entry: NavEntry,
): NavStack {
    return NavStack(id, listOf(entry))
}

public fun NavStack.then(entry: NavEntry): NavStack = copy(entries = entries + entry)

public class NavStackBuilder @PublishedApi internal constructor(
    @PublishedApi internal val id: String,
    entries: List<NavEntry>,
) {

    @PublishedApi
    internal val entries: MutableList<NavEntry> = entries.toMutableList()

    public fun add(entry: NavEntry): NavStackBuilder = apply {
        entries += entry
    }

    public fun add(vararg entries: NavEntry): NavStackBuilder = apply {
        this.entries += entries
    }

    public fun addAll(entries: Collection<NavEntry>): NavStackBuilder = apply {
        this.entries += entries
    }

    public fun add(dest: NavDest, tags: List<Any> = emptyList()): NavStackBuilder = apply {
        add(NavEntry(dest, tags))
    }
}

public inline fun buildNavStack(
    id: String,
    body: NavStackBuilder.() -> Unit
): NavStack {
    return NavStackBuilder(id, entries = emptyList())
        .apply(body)
        .let { builder -> NavStack(builder.id, builder.entries.toList()) }
}
