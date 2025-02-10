@file:OptIn(ExperimentalCoroutinesApi::class)

package dev.androidbroadcast.navstate.stack

import dev.androidbroadcast.navstate.NavCommand
import dev.androidbroadcast.navstate.NavStructure.Id
import dev.androidbroadcast.navstate.Navigator
import dev.androidbroadcast.navstate.buildNavState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private val NAV_STACK_DEFAULT = Id("default")

class NavigationCommandTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
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
                    setCurrent = true
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
                    setCurrent = true
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
                    setCurrent = true
                )
            }
        val navigator = Navigator(initialState)
        navigator.enqueue(
            NavCommand.popTop()
                .forward(TestNavDestinations.Details("testId")),
        )

        val expectedState =
            buildNavState {
                add(
                    buildNavStack(id = NAV_STACK_DEFAULT) {
                        add(TestNavDestinations.Root)
                        add(TestNavDestinations.Details("testId"))
                    },
                    setCurrent = true
                )
            }
        assertEquals(expectedState, navigator.currentState)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
