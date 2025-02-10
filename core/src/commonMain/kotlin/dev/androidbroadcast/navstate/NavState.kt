package dev.androidbroadcast.navstate

import kotlinx.serialization.Serializable

@Serializable
public data class NavState(
    public val structures: Set<NavStructure>,
    public val currentId: NavStructure.Id,
) : Set<NavStructure> by structures {

    public val current: NavEntriesStructure by lazy {
        structures.first { it.id == currentId } as NavEntriesStructure
    }

    public companion object {

        public val DefaultStructId: NavStructure.Id = NavStructure.Id("default")
    }
}

public operator fun NavState.get(id: NavStructure.Id): NavStructure? {
    return structures.firstNotNullOfOrNull { structure -> structure[id] }
}

public fun NavState.then(struct: NavStructure): NavState {
    return copy(
        structures = buildSet {
            addAll(this@then.structures)
            add(struct)
        },
    )
}

public inline fun NavState.buildNavState(
    body: NavStateBuilder.() -> Unit
): NavState {
    return buildNavStateInternal(
        NavStateBuilder(
            structures = this.structures,
            currentStructId = this.currentId,
        ),
        body,
    )
}

public inline fun buildNavState(
    body: NavStateBuilder.() -> Unit
): NavState {
    return buildNavStateInternal(
        NavStateBuilder(
            structures = emptySet(),
        ),
        body,
    )
}

@PublishedApi
internal inline fun buildNavStateInternal(
    builder: NavStateBuilder,
    body: NavStateBuilder.() -> Unit
): NavState = with(builder.apply(body)) {
    NavState(
        structures = structs.values.toSet(),
        currentId = requireNotNull(currentStructId) { "Current structure must be set" },
    )
}

public class NavStateBuilder @PublishedApi internal constructor(
    structures: Set<NavStructure>,
    @PublishedApi
    internal var currentStructId: NavStructure.Id? = null
) {

    @PublishedApi
    internal val structs: MutableMap<NavStructure.Id, NavStructure> =
        structures.associateBy(keySelector = { it.id })
            .toMutableMap()

    public fun setCurrent(id: NavStructure.Id?): NavStateBuilder = apply {
        this.currentStructId = id
    }

    public fun add(struct: NavStructure): NavStateBuilder = apply {
        structs[struct.id] = struct
    }

    public fun add(struct: NavEntriesStructure, setCurrent: Boolean = false): NavStateBuilder = apply {
        structs[struct.id] = struct
        if (setCurrent) setCurrent(struct.id)
    }

    public fun add(vararg structs: NavStructure): NavStateBuilder = apply {
        this.structs += structs.associateBy { it.id }
    }

    public fun addAll(structs: Collection<NavStructure>): NavStateBuilder = apply {
        this.structs += structs.associateBy { it.id }
    }
}

public fun NavState.transform(command: NavCommand): NavState = command.transform(this)
