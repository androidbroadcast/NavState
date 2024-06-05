package dev.androidbroadcast.navstate

import kotlinx.serialization.Serializable

@Serializable
public data class NavState(
    val entries: List<NavEntry>
) : List<NavEntry> by entries

public fun NavState.then(entry: NavEntry): NavState = NavState(entries + entry)

public fun NavState.buildNavState(
    body: NavStateBuilder.() -> Unit
): NavState {
    val entries = NavStateBuilder(this.entries).apply(body).entries.toList()
    return NavState(entries)
}

public fun buildNavState(
    body: NavStateBuilder.() -> Unit
): NavState {
    val entries = NavStateBuilder().apply(body).entries.toList()
    return NavState(entries)
}

public class NavStateBuilder internal constructor(
    entries: List<NavEntry> = emptyList()
) {

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
