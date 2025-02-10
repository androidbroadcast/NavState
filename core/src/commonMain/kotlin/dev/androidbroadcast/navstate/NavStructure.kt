package dev.androidbroadcast.navstate

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

public interface NavStructure {

    public val id: Id

    public val parent: NavStructure?
        get() = null

    public operator fun contains(structure: NavStructure): Boolean = when {
        this === structure -> true
        this is NavGroup -> structures.any { structure in it }
        else -> false
    }

    @JvmInline
    @Serializable
    public value class Id(public val id: String) {

        init {
            check(id.isNotBlank())
        }
    }
}

public interface NavEntriesStructure : NavStructure, Iterable<NavEntry> {

    public val entries: List<NavEntry>

    public operator fun contains(entry: NavEntry): Boolean = entry in entries
}

public operator fun NavStructure.contains(entry: NavEntry): Boolean = when (this) {
    is NavGroup -> structures.any { entry in it }
    is NavEntriesStructure -> contains(entry)
    else -> false
}

public operator fun NavStructure.get(id: NavStructure.Id): NavStructure? = when {
    this.id == id -> this
    this is NavGroup -> get(id)
    else -> null
}
