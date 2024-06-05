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

class NavigationCommandTest {

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `back works correctly`() {
        val initialState =
            buildNavState {
                add(TestNavDestinations.Root)
                add(TestNavDestinations.DataList)
                add(TestNavDestinations.Details("testId"))
            }

        val navigator = Navigator(initialState)
        navigator.enqueue(Back())

        val expectedState =
            buildNavState {
                add(TestNavDestinations.Root)
                add(TestNavDestinations.DataList)
            }
        assertEquals(expectedState, navigator.currentState)
    }

    @Test
    fun `forward works correctly`() {
        val initialState =
            buildNavState {
                add(TestNavDestinations.Root)
                add(TestNavDestinations.DataList)
            }

        val navigator = Navigator(initialState)
        navigator.enqueue(Forward(TestNavDestinations.Details("testId")))

        val expectedState =
            buildNavState {
                add(TestNavDestinations.Root)
                add(TestNavDestinations.DataList)
                add(TestNavDestinations.Details("testId"))
            }
        assertEquals(expectedState, navigator.currentState)
    }

    @Test
    fun `backTo root works correctly`() {
        val root = NavEntry(TestNavDestinations.Root, tags = listOf("root"))
        val initialState =
            buildNavState {
                add(root.copy())
                add(TestNavDestinations.DataList)
                add(TestNavDestinations.Details("testId"))
            }

        val navigator = Navigator(initialState)
        navigator.enqueue(BackTo(tag = "root"))

        val expectedState =
            buildNavState {
                add(root.copy())
            }
        assertEquals(expectedState, navigator.currentState)
    }

    @Test
    fun `back in root has no effect`() {
        val initialState =
            buildNavState {
                add(NavEntry(TestNavDestinations.Root))
            }

        val navigator = Navigator(initialState)
        navigator.enqueue(Back())

        val expectedState =
            buildNavState {
                add(NavEntry(TestNavDestinations.Root))
            }
        assertEquals(expectedState, navigator.currentState)
    }

    @Test
    fun `replace top works correctly`() {
        val initialState =
            buildNavState {
                add(TestNavDestinations.Root)
                add(TestNavDestinations.DataList)
            }

        val replaceNavState: NavCommand = PopTop() + Forward(TestNavDestinations.Details("testId"))

        val navigator = Navigator(initialState)
        navigator.enqueue(replaceNavState)

        val expectedState =
            buildNavState {
                add(TestNavDestinations.Root)
                add(TestNavDestinations.Details("testId"))
            }
        assertEquals(expectedState, navigator.currentState)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
