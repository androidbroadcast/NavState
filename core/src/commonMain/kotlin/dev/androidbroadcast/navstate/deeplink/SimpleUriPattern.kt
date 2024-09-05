package dev.androidbroadcast.navstate.deeplink

/**
 * <scheme>://<host>/<path with params>
 * <param> - {name} can contains a-Z, 0-9, `-`
 */
public data class SimpleUriPattern(
    val schemeRegex: String,
    val hostRegex: String,
    /**
     * Uri path. Can have params {name}
     */
    val pathRegex: String,
) : UriMatcher {

    /**
     * All params found in [pathRegex]
     */
    private val pathParams = mutableSetOf<String>()

    private val uriRegex = Regex(
        prepareScheme(schemeRegex) +
                Regex.escape("://") +
                prepareHost(hostRegex) +
                preparePath(pathRegex, pathParams),
    )

    override fun matches(uri: String): Boolean = uriRegex.matches(uri)

    override fun match(uri: String): MatchResult? {
        val matchResult = uriRegex.matchEntire(uri) ?: return null
        return MatchResult(
            uri = uri,
            path = matchResult.findGroup(GROUP_PATH),
            host = matchResult.findGroup(GROUP_HOST),
            scheme = matchResult.findGroup(GROUP_SCHEME),
            pathParams = pathParams.associateWith { matchResult.findGroup(it) },
        )
    }

    override fun toString(): String = "$schemeRegex://$hostRegex/$pathRegex"

    public companion object {

        public fun any(
            schemeRegex: String = ANY_REGEX,
            hostRegex: String = ANY_REGEX,
            pathRegex: String = ANY_REGEX,
        ): SimpleUriPattern {
            return SimpleUriPattern(schemeRegex, hostRegex, pathRegex)
        }
    }

    public data class MatchResult(
        override val uri: String,
        override val path: String,
        override val host: String,
        override val scheme: String,
        override val pathParams: Map<String, String>,
    ) : UriMatcher.MatchResult
}

private fun MatchResult.findGroup(key: String): String {
    return groups[key]?.value
        ?: throw IncorrectUriFormat("Group with key `$key` was not found")
}

private const val GROUP_SCHEME = "scheme"
private const val GROUP_HOST = "host"
private const val GROUP_PATH = "path"
private const val PATH_PARAM_REGEX = "[\\w-]+"
private const val HOST_PORT_REGEX = "[\\w{}.]+)(:\\d+)?\\Q/\\E?"
private const val PATH_REGEX = "[\\w{}\\-_/?]+)?"
private const val SCHEME_REGEX = "[\\w{}]+)\\Q://\\E"
private const val ANY_REGEX: String = "*"

private val FULL_URI_REGEX = Regex(
    "(?<$GROUP_SCHEME>$SCHEME_REGEX" +
            "(?<$GROUP_HOST>$HOST_PORT_REGEX" +
            "(?<$GROUP_PATH>$PATH_REGEX",
)

private fun preparePath(path: String, pathParams: MutableSet<String>): String {
    if (path == ANY_REGEX) {
        // Any path will be valid
        return "/?(?<$GROUP_PATH>[\\w-_/]*)"
    } else {
        return buildString {
            append("(?<$GROUP_PATH>")
            if (!path.startsWith('/')) {
                append('/')
            }
            append(
                // Need to escape { and } to run on Android API Level 34 and lower
                path.replace(Regex("\\{(?<param>$PATH_PARAM_REGEX)\\}")) { result ->
                    val param = requireNotNull(result.groups["param"]).value
                    require(pathParams.add(param)) {
                        "Duplicated parameter $param in path"
                    }
                    "(?<$param>$PATH_PARAM_REGEX)"
                },
            )
            append(')')
        }
    }
}

private fun prepareHost(host: String): String {
    return if (host == ANY_REGEX) "(?<$GROUP_HOST>[\\w-_.]+)" else "(?<$GROUP_HOST>\\Q$host\\E)"
}

private fun prepareScheme(scheme: String): String {
    return if (scheme == ANY_REGEX) "(?<$GROUP_SCHEME>[\\w]+)" else "(?<$GROUP_SCHEME>\\Q$scheme\\E)"
}

public fun SimpleUriPattern(uriPattern: String): SimpleUriPattern {
    val matchResult = requireNotNull(FULL_URI_REGEX.matchEntire(uriPattern)) {
        "Illegal uri pattern format '$uriPattern'"
    }
    return SimpleUriPattern(
        schemeRegex = matchResult.groups[GROUP_SCHEME]!!.value.takeIf { it.isNotEmpty() }
            ?: ANY_REGEX,
        hostRegex = matchResult.groups[GROUP_HOST]!!.value.takeIf { it.isNotEmpty() } ?: ANY_REGEX,
        pathRegex = matchResult.groups[GROUP_PATH]!!.value.takeIf { it.isNotEmpty() } ?: ANY_REGEX,
    )
}

