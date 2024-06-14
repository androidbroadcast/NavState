package dev.androidbroadcast.navstate.annotations

import dev.androidbroadcast.navstate.NavDest
import dev.androidbroadcast.navstate.NavStack
import dev.androidbroadcast.navstate.NavState
import kotlin.reflect.KClass

/**
 * Describe [NavDest] connected with @Composable function
 */
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION)
public annotation class NavDest(
    val dest: KClass<out NavDest>
)
