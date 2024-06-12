package dev.androidbroadcast.navstate

import kotlinx.serialization.Serializable

@Serializable
public data class NavState(
    val stacks: Set<NavStack>,
    val activeStack: NavStack,
) : Set<NavStack> by stacks {

    public companion object {

        public const val DefaultStackId: String = "default"
    }
}

public fun NavState(
    initialDest: NavDest,
    stackId: String = NavState.DefaultStackId,
): NavState {
    val stack = NavStack(stackId, listOf(NavEntry(initialDest)))
    return NavState(
        stacks = setOf(stack),
        activeStack = stack,
    )
}

internal fun NavState.validate() {
    check(stacks.isNotEmpty()) { "NavState must have at least 1 NavStack" }
    check(activeStack in stacks) { "Active Stack isn't in stacks" }
    stacks.forEach(NavStack::validate)
}

public fun NavState.then(stack: NavStack): NavState {
    return copy(stacks = this.stacks + stack)
}

public inline fun NavState.buildNavState(
    body: NavStateBuilder.() -> Unit
): NavState {
    return buildNavStateInternal(
        NavStateBuilder(
            stacks = this.stacks,
            activeStackId = activeStack.id,
        ),
        body,
    )
}

public inline fun buildNavState(
    body: NavStateBuilder.() -> Unit
): NavState {
    return buildNavStateInternal(
        NavStateBuilder(
            stacks = emptySet(),
            activeStackId = null,
        ),
        body,
    )
}

@PublishedApi
internal inline fun buildNavStateInternal(
    builder: NavStateBuilder,
    body: NavStateBuilder.() -> Unit
): NavState = with(builder.apply(body)) {
    val activeStack = checkNotNull(activeStack) { "Active stack isn't set" }
    NavState(stacks.values.toSet(), activeStack)
}

public class NavStateBuilder @PublishedApi internal constructor(
    stacks: Set<NavStack>,
    private var activeStackId: String?,
) {

    @PublishedApi
    internal val activeStack: NavStack?
        get() = activeStackId?.let(stacks::getValue)

    @PublishedApi
    internal val stacks: MutableMap<String, NavStack> =
        stacks.associateBy(keySelector = { it.id })
            .toMutableMap()

    public fun add(stack: NavStack, makeActive: Boolean = false): NavStateBuilder = apply {
        stacks[stack.id] = stack
        if (makeActive) activeStackId = stack.id
    }

    public fun add(vararg stacks: NavStack): NavStateBuilder = apply {
        this.stacks += stacks.associateBy { it.id }
    }

    public fun addAll(stacks: Collection<NavStack>): NavStateBuilder = apply {
        this.stacks += stacks.associateBy { it.id }
    }

    public fun setActive(stack: NavStack): NavStateBuilder = apply {
        activeStackId = stack.id
    }

    public fun setActive(stackId: String): NavStateBuilder = apply {
        activeStackId = stackId
    }
}
