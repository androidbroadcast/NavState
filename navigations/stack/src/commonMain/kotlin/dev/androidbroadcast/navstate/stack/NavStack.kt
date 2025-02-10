package dev.androidbroadcast.navstate.stack

import dev.androidbroadcast.navstate.NavDest
import dev.androidbroadcast.navstate.NavEntriesStructure
import dev.androidbroadcast.navstate.NavEntry
import dev.androidbroadcast.navstate.NavStructure
import kotlinx.serialization.Serializable

@Serializable
public data class NavStack(
    override val id: NavStructure.Id,
    override val entries: List<NavEntry>,
    override val parent: NavStructure? = null,
) : NavEntriesStructure, Iterable<NavEntry> by entries.reversed() {

    init {
        check(entries.isNotEmpty()) {
            "NavStack must have at least one NavEntry"
        }
    }
}

public val NavStack.current: NavEntry
    get() = entries.last()

public fun NavStack(
    id: NavStructure.Id,
    entry: NavEntry,
    parent: NavStructure? = null
): NavStack {
    return NavStack(id, listOf(entry), parent)
}

public class NavStackBuilder @PublishedApi internal constructor(
    @PublishedApi internal val id: NavStructure.Id,
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

    public fun addAll(entries: Iterable<NavEntry>): NavStackBuilder = apply {
        this.entries += entries
    }

    public fun add(dest: NavDest, tags: List<Any> = emptyList()): NavStackBuilder = apply {
        add(NavEntry(dest, tags))
    }
}

public inline fun buildNavStack(
    id: NavStructure.Id,
    body: NavStackBuilder.() -> Unit
): NavStack {
    return NavStackBuilder(id, entries = emptyList())
        .apply(body)
        .let { builder -> NavStack(builder.id, builder.entries.toList()) }
}
