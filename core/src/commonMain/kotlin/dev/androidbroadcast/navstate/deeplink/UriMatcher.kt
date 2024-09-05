package dev.androidbroadcast.navstate.deeplink

/**
 * Check that input Uri matches you pattern and you can handle it
 */
public interface UriMatcher {

    public fun matches(uri: String): Boolean

    public fun match(uri: String): MatchResult?

    public interface MatchResult {
        public val uri: String
        public val path: String
        public val host: String
        public val scheme: String
        public val pathParams: Map<String, String>
    }
}
