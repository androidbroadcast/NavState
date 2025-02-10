package dev.androidbroadcast.navstate.sample.android.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.androidbroadcast.navstate.NavCommand
import dev.androidbroadcast.navstate.NavDest
import dev.androidbroadcast.navstate.NavEntry
import dev.androidbroadcast.navstate.NavHost
import dev.androidbroadcast.navstate.NavState
import dev.androidbroadcast.navstate.Navigator
import dev.androidbroadcast.navstate.buildNavState
import dev.androidbroadcast.navstate.deeplink.SimpleUriPattern
import dev.androidbroadcast.navstate.rememberNavTopEntry
import dev.androidbroadcast.navstate.replaceState
import dev.androidbroadcast.navstate.sample.android.BroadcastNavGraph
import dev.androidbroadcast.navstate.sample.android.BroadcastNavGraph.WebPageDest
import dev.androidbroadcast.navstate.sample.android.data.ArticleId
import dev.androidbroadcast.navstate.sample.android.ui.theme.BroadcastTheme
import dev.androidbroadcast.navstate.stack.NavStack
import dev.androidbroadcast.navstate.stack.buildNavStack
import dev.androidbroadcast.navstate.stack.popTop


@Composable
fun RootScreen(
    onRootBack: () -> Unit,
    openWebPage: (String) -> Boolean,
) {
    val navigator = setupNavigator()
    BroadcastTheme {
        Box(Modifier.fillMaxHeight()) {
            NavHost(navigator, onRootBack) {
                Scaffold(
                    bottomBar = {
                        BottomAppBar {

                        }
                    },
                    content = { paddings ->
                        Box(Modifier.padding(paddings)) {
                            when (val dest = rememberNavTopEntry().destination) {
                                // Открытие ссылки вне приложения, делегируем приложению
                                is WebPageDest -> {
                                    openWebPage(dest.url)
                                    navigator.enqueue(NavCommand.popTop())
                                }

                                else -> ScreenForDestination(dest)
                            }
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun ScreenForDestination(dest: NavDest) {
    when (dest) {
        is BroadcastNavGraph.NavMenu -> NavigationMenuScreen()
        is BroadcastNavGraph.Article -> ArticleItemScreen(dest.articleId)
        is BroadcastNavGraph.Articles -> ArticlesScreen()
        is BroadcastNavGraph.BroadcastResources -> BroadcastResourcesScreen()
        is BroadcastNavGraph.AboutAuthor -> AboutAuthorScreen()
        else -> error("Unknown destination $dest")
    }
}

private fun setupNavigator(): Navigator {
    val navStack = NavStack(id = NavState.DefaultStructId, entry = NavEntry(BroadcastNavGraph.root))
    val initialState = NavState(setOf(navStack), navStack.id)
    return Navigator(initialState).apply {
        registerDeepLink(
            matcher = SimpleUriPattern.any(
                schemeRegex = "(https?|broadcast)",
                hostRegex = Regex.escape("androidbroadcast.dev"),
                pathRegex = "article/{articleId}",
            ),
        ) { navigator, _, result ->
            val articleId = result.params.getValue("articleId")
            handleArticleDeepLink(navigator, articleId)
            return@registerDeepLink true //
        }

        registerDeepLink(
            matcher = SimpleUriPattern.any(
                schemeRegex = "(https?|broadcast)",
                hostRegex = Regex.escape("androidbroadcast.dev"),
                pathRegex = "author",
            ),
        ) { navigator, _, _ ->
            handleAboutAuthorDeepLink(navigator)
            return@registerDeepLink true //
        }
    }
}

private fun Navigator.handleAboutAuthorDeepLink(navigator: Navigator) {
    navigator.replaceState {
        currentState.buildNavState {
            add(
                buildNavStack(NavState.DefaultStructId) {
                    add(NavEntry(BroadcastNavGraph.root))
                    add(NavEntry(BroadcastNavGraph.AboutAuthor()))
                },
            )
        }
    }
}

private fun Navigator.handleArticleDeepLink(
    navigator: Navigator,
    articleId: String
) {
    navigator.replaceState {
        currentState.buildNavState {
            add(
                buildNavStack(NavState.DefaultStructId) {
                    add(NavEntry(BroadcastNavGraph.root))
                    add(NavEntry(BroadcastNavGraph.Articles()))
                    add(NavEntry(BroadcastNavGraph.Article(ArticleId(articleId))))
                },
            )
        }
    }
}
