package dev.androidbroadcast.navstate


internal fun <E> Collection<E>.hasDuplicates(): Boolean {
    return all { item -> count { item == it } == 1 }
}
