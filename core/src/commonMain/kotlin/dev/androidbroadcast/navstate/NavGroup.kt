package dev.androidbroadcast.navstate

public data class NavGroup(
    override val id: NavStructure.Id,
    val structures: List<NavStructure>,
    val current: NavStructure = structures.first(),
) : NavStructure, Iterable<NavStructure> by structures {

    init {
        check(structures.isNotEmpty())

        // Check that entries doesn't contain duplicates
        check(!structures.hasDuplicates())
        check(current in structures)
    }

    public operator fun get(id: NavStructure.Id): NavStructure? {
        return structures.firstNotNullOfOrNull { structure -> structure[id] }
    }
}
