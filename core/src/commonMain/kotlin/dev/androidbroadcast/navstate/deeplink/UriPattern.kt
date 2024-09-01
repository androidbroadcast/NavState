package dev.androidbroadcast.navstate.deeplink

public data class UriPattern(
    val scheme: String = ANY,
    val host: String = ANY,
    val path: String = ANY,
) {

    private val pathParams = mutableSetOf<String>()

    private val uriRegex = Regex(
        prepareScheme(scheme) +
            "\\Q://\\E" +
            prepareHost(host) +
            preparePath(path, pathParams),
    )

    public fun matches(uri: String): Boolean {
        return uriRegex.matches(uri)
    }

    public fun match(uri: String): MatchResult? {
        val matchResult = uriRegex.matchEntire(uri) ?: return null
        return MatchResult(
            uri = uri,
            path = matchResult.findGroup(GROUP_PATH),
            host = matchResult.findGroup(GROUP_HOST),
            scheme = matchResult.findGroup(GROUP_SCHEME),
            pathParams = pathParams.associateWith { matchResult.findGroup(it) },
        )
    }

    override fun toString(): String = "$scheme://$host/$path"

    public data class MatchResult(
        val uri: String,
        val path: String,
        val host: String,
        val scheme: String,
        val pathParams: Map<String, String>,
    )

    public companion object {

        public const val ANY: String = "*"
    }
}

private fun MatchResult.findGroup(key: String): String = groups[key]?.value ?: throw IncorrectUriFormat()

private const val GROUP_SCHEME = "scheme"
private const val GROUP_HOST = "host"
private const val GROUP_PATH = "path"

private val FULL_URI_REGEX = Regex(
    "(?<$GROUP_SCHEME>[\\w{}]+)\\Q://\\E" +
        "(?<$GROUP_HOST>[\\w{}.]+)\\Q/\\E?" +
        "(?<$GROUP_PATH>[\\w{}\\-_/?]+)?",
)

public fun preparePath(path: String, pathParams: MutableSet<String>): String {
    if (path == UriPattern.ANY) {
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
                path.replace(Regex("\\{(?<param>[\\w-]+)\\}")) { result ->
                    val param = requireNotNull(result.groups["param"]).value
                    require(pathParams.add(param)) {
                        "Duplicated parameter $param in path"
                    }
                    "(?<$param>[\\w_]+)"
                },
            )
            append(')')
        }
    }
}

public fun prepareHost(host: String): String {
    return if (host == UriPattern.ANY) "(?<$GROUP_HOST>[\\w-_.]+)" else "(?<$GROUP_HOST>\\Q$host\\E)"
}

public fun prepareScheme(scheme: String): String {
    if (scheme == UriPattern.ANY) {
        return "(?<$GROUP_SCHEME>[\\w]+)"
    } else {
        return "(?<$GROUP_SCHEME>\\Q$scheme\\E)"
    }
}

@Suppress("FunctionName")
public fun UriMatcher(uriPattern: String): UriPattern {
    val matchResult = requireNotNull(FULL_URI_REGEX.matchEntire(uriPattern)) {
        "Illegal uri pattern format '$uriPattern'"
    }
    return UriPattern(
        scheme = matchResult.groups[GROUP_SCHEME]!!.value.takeIf { it.isNotEmpty() }
            ?: UriPattern.ANY,
        host = matchResult.groups[GROUP_HOST]!!.value.takeIf { it.isNotEmpty() } ?: UriPattern.ANY,
        path = matchResult.groups[GROUP_PATH]!!.value.takeIf { it.isNotEmpty() } ?: UriPattern.ANY,
    )
}

public class IncorrectUriFormat : Exception()
