package dev.androidbroadcast.navstate

import kotlinx.serialization.Serializable

@Serializable
public data class NavStack(
    val id: String,
    val entries: List<NavEntry>
)

internal fun NavStack.validate() {
    check(entries.isNotEmpty()) {
        "NavStack must have at least 1 NavEntry"
    }
}

public fun NavStack.then(entry: NavEntry): NavStack = copy(entries = entries + entry)

public class NavStackBuilder @PublishedApi internal constructor(
    @PublishedApi internal val id: String,
    entries: List<NavEntry>,
) {

    @PublishedApi
    internal val entries: MutableList<NavEntry> = entries.toMutableList()

    public fun add(entry: NavEntry) {
        entries += entry
    }

    public fun add(vararg entries: NavEntry) {
        this.entries += entries
    }

    public fun addAll(entries: Collection<NavEntry>) {
        this.entries += entries
    }

    public fun add(dest: NavDest, tags: List<Any> = emptyList()) {
        add(NavEntry(dest, tags))
    }
}

public inline fun NavStack.buildNavStack(
    id: String,
    body: NavStackBuilder.() -> Unit
): NavStack {
    return buildNavStackInternal(NavStackBuilder(id, entries = this.entries), body)
}

public fun buildNavStack(
    id: String,
    body: NavStackBuilder.() -> Unit
): NavStack {
    return buildNavStackInternal(NavStackBuilder(id, entries = emptyList()), body)
}

@PublishedApi
internal inline fun buildNavStackInternal(
    builder: NavStackBuilder,
    body: NavStackBuilder.() -> Unit
): NavStack = with(builder.apply(body)) {
    return NavStack(builder.id, entries.toList())
}
