package dev.androidbroadcast.navstate.deeplink

/**
 * Check that input Uri matches you pattern and you can handle it
 */
public fun interface UriMatcher {

    /**
     * Check if input Uri matches you pattern or not.
     *
     * @param uri Uri to check
     *
     * @return matched pattern or null if not matched
     */
    public fun match(uri: String): MatchResult?

    /**
     * Check that input Uri matches you pattern and you can handle it
     */
    public fun matches(uri: String): Boolean = match(uri) != null

    public interface MatchResult {

        public val uri: String
        public val path: String
        public val host: String
        public val scheme: String

        /**
         * Values of all params in [uri]
         */
        public val params: Map<String, String>
    }
}
