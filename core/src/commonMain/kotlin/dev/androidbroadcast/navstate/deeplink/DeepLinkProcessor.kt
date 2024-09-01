package dev.androidbroadcast.navstate.deeplink

public interface DeepLinkProcessor {

    public fun registerDeepLink(pattern: UriPattern, handler: DeepLinkHandler)

    public fun open(uri: String): Boolean

    public fun interface DeepLinkHandler {

        public fun handle(uri: String, result: UriPattern.MatchResult): Boolean
    }
}

internal class SimpleDeepLinkProcessor : DeepLinkProcessor{

    private val deepLinks = mutableMapOf<UriPattern, DeepLinkProcessor.DeepLinkHandler>()

    override fun registerDeepLink(pattern: UriPattern, handler: DeepLinkProcessor.DeepLinkHandler) {
        deepLinks[pattern] = handler
    }

    override fun open(uri: String): Boolean {
        var urlHandled = false
        deepLinks.forEach { (pattern, handler) ->
            val result = pattern.match(uri)
            if (result != null) {
                handler.handle(uri, result)
                urlHandled = true
            }
        }
        return urlHandled
    }
}
