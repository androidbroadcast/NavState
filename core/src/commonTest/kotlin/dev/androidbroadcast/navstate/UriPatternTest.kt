package dev.androidbroadcast.navstate

import dev.androidbroadcast.navstate.deeplink.UriPattern
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class UriPatternTest {

    @Test
    fun `simple uri matching`() {
        val matcher = UriPattern(
            scheme = "https",
            host = "google.com",
        )
        assertTrue { matcher.matches("https://google.com") }
    }

    @Test
    fun test2() {
        val matcher = UriPattern(
            scheme = "https",
            host = "google.com",
        )
        assertTrue { matcher.matches("https://google.com/path") }
    }

    @Test
    fun `matcher with fixed scheme and host with param in path works correctly`() {
        val matcher = UriPattern(
            scheme = "https",
            host = "google.com",
            path = "path/{id}",
        )
        val uri = "https://google.com/path/1"
        val result = matcher.match(uri)
        val expected: UriPattern.MatchResult = UriPattern.MatchResult(
            uri = uri,
            scheme = "https",
            host = "google.com",
            path = "/path/1",
            pathParams = mapOf("id" to "1"),
        )
        assertEquals(expected, result)
    }

    @Test
    fun test4() {
        val matcher = UriPattern(
            scheme = "navstate",
            host = "entry",
            path = "/page/{pageNumber}/line/{lineNumber}",
        )
        val uri = "navstate://entry/page/51/line/143"

        val expected: UriPattern.MatchResult = UriPattern.MatchResult(
            uri = uri,
            scheme = "navstate",
            host = "entry",
            path = "/page/51/line/143",
            pathParams = mapOf("pageNumber" to "51", "lineNumber" to "143"),
        )

        val result: UriPattern.MatchResult? = matcher.match(uri)
        assertEquals(expected, result)
    }
}
