@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.androidbroadcast.navstate

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private const val NAV_STACK_DEFAULT = "default"

class NavigationCommandTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `back works correctly`() {
        val initialState =
            buildNavState {
                add(
                    buildNavStack(id = NAV_STACK_DEFAULT) {
                        add(TestNavDestinations.Root)
                        add(TestNavDestinations.DataList)
                        add(TestNavDestinations.Details("testId"))
                    },
                    makeActive = true,
                )
            }

        val navigator = Navigator(initialState)
        navigator.enqueue(NavCommand.back())

        val expectedState =
            buildNavState {
                add(
                    buildNavStack(id = NAV_STACK_DEFAULT) {
                        add(TestNavDestinations.Root)
                        add(TestNavDestinations.DataList)
                    },
                    makeActive = true,
                )
            }
        assertEquals(expectedState, navigator.currentState)
    }

    @Test
    fun `forward works correctly`() {
        val initialState =
            buildNavState {
                add(
                    buildNavStack(id = NAV_STACK_DEFAULT) {
                        add(TestNavDestinations.Root)
                        add(TestNavDestinations.DataList)
                    },
                    makeActive = true,
                )
            }

        val navigator = Navigator(initialState)
        navigator.enqueue(
            NavCommand.forward(TestNavDestinations.Details("testId")),
        )

        val expectedState =
            buildNavState {
                add(
                    buildNavStack(id = NAV_STACK_DEFAULT) {
                        add(TestNavDestinations.Root)
                        add(TestNavDestinations.DataList)
                        add(TestNavDestinations.Details("testId"))
                    },
                    makeActive = true,
                )
            }
        assertEquals(expectedState, navigator.currentState)
    }

    @Test
    fun `back in root has no effect`() {
        val initialState =
            buildNavState {
                add(
                    buildNavStack(id = NAV_STACK_DEFAULT) {
                        add(NavEntry(TestNavDestinations.Root))
                    },
                    makeActive = true,
                )
            }

        val navigator = Navigator(initialState)
        navigator.enqueue(NavCommand.popTop(count = 1))

        val expectedState =
            buildNavState {
                add(
                    buildNavStack(id = NAV_STACK_DEFAULT) {
                        add(NavEntry(TestNavDestinations.Root))
                    },
                    makeActive = true,
                )
            }
        assertEquals(expectedState, navigator.currentState)
    }

    @Test
    fun `replace top works correctly`() {
        val initialState =
            buildNavState {
                add(
                    buildNavStack(id = NAV_STACK_DEFAULT) {
                        add(TestNavDestinations.Root)
                        add(TestNavDestinations.DataList)
                    },
                    makeActive = true,
                )
            }

        val replaceNavState: NavCommand = NavCommand.popTop()
            .forward(TestNavDestinations.Details("testId"))

        val navigator = Navigator(initialState)
        navigator.enqueue(replaceNavState)

        val expectedState =
            buildNavState {
                add(
                    buildNavStack(id = NAV_STACK_DEFAULT) {
                        add(TestNavDestinations.Root)
                        add(TestNavDestinations.Details("testId"))
                    },
                    makeActive = true,
                )
            }
        assertEquals(expectedState, navigator.currentState)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
