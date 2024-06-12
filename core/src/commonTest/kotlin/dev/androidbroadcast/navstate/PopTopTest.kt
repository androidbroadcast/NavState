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

class PopTopTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `popTop works correctly`() {
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
        navigator.enqueue(NavCommand.popTop())

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
    fun `popTop single entry in root has no effect`() {
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
    fun `After popTop 2 entries in 2 entry Stack root entry remains`() {
        val initialState =
            buildNavState {
                add(
                    buildNavStack(id = NAV_STACK_DEFAULT) {
                        add(NavEntry(TestNavDestinations.Root))
                        add(NavEntry(TestNavDestinations.DataList))
                    },
                    makeActive = true,
                )
            }

        val navigator = Navigator(initialState)
        navigator.enqueue(NavCommand.popTop(count = 2))

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

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
