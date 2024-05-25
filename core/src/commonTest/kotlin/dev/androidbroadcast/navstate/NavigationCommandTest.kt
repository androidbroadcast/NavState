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
        val initialState = buildNavState {
            add(TestDestinations.Root)
            add(TestDestinations.DataList)
            add(TestDestinations.Details("testId"))
        }

        val navigator = Navigator(initialState)
        navigator.back()

        val expectedState = buildNavState {
            add(TestDestinations.Root)
            add(TestDestinations.DataList)
        }
        assertEquals(expectedState, navigator.currentState)
    }

    @Test
    fun `forward works correctly`() {
        val initialState = buildNavState {
            add(TestDestinations.Root)
            add(TestDestinations.DataList)
        }

        val navigator = Navigator(initialState)
        navigator.forward(TestDestinations.Details("testId"))

        val expectedState = buildNavState {
            add(TestDestinations.Root)
            add(TestDestinations.DataList)
            add(TestDestinations.Details("testId"))
        }
        assertEquals(expectedState, navigator.currentState)
    }

    @Test
    fun `backTo root works correctly`() {
        val root = NavEntry(TestDestinations.Root, tags = listOf("root"))
        val initialState = buildNavState {
            add(root.copy())
            add(TestDestinations.DataList)
            add(TestDestinations.Details("testId"))
        }

        val navigator = Navigator(initialState)
        navigator.backTo(tag = "root")

        val expectedState = buildNavState {
            add(root.copy())
        }
        assertEquals(expectedState, navigator.currentState)
    }

    @Test
    fun `back in root has no effect`() {
        val initialState = buildNavState {
            add(NavEntry(TestDestinations.Root))
        }

        val navigator = Navigator(initialState)
        navigator.back()

        val expectedState = buildNavState {
            add(NavEntry(TestDestinations.Root))
        }
        assertEquals(expectedState, navigator.currentState)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }
}