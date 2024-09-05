package dev.androidbroadcast.navstate.sample.android.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.androidbroadcast.navstate.NavCommand
import dev.androidbroadcast.navstate.NavHost
import dev.androidbroadcast.navstate.NavState
import dev.androidbroadcast.navstate.Navigator
import dev.androidbroadcast.navstate.enqueue
import dev.androidbroadcast.navstate.popTop
import dev.androidbroadcast.navstate.rememberNavTopEntry
import dev.androidbroadcast.navstate.sample.android.BroadcastNavGraph
import dev.androidbroadcast.navstate.sample.android.ui.theme.BroadcastTheme

@Composable
fun RootScreen(
    onRootBack: () -> Unit,
    openWebPage: (String) -> Boolean,
) {
    val navigator = setupNavigator()
    BroadcastTheme {
        Box(Modifier.fillMaxHeight()) {
            NavHost(navigator, onRootBack) {
                when (val dest = rememberNavTopEntry().destination) {
                    is BroadcastNavGraph.NavMenu -> NavigationMenuScreen()
                    is BroadcastNavGraph.Article -> ArticleItemScreen(dest.articleId)
                    is BroadcastNavGraph.Articles -> ArticlesScreen()
                    is BroadcastNavGraph.BroadcastResources -> BroadcastResourcesScreen()
                    is BroadcastNavGraph.AboutAuthor -> AboutAuthorScreen()
                    // Открытие ссылки вне приложения, делегируем приложению
                    is BroadcastNavGraph.WebPageDest -> {
                        openWebPage(dest.url)
                        navigator.enqueue(NavCommand.popTop())
                    }

                    else -> error("Unknown destination $dest")
                }
            }
        }
    }
}

private fun setupNavigator(): Navigator {
    return Navigator(NavState(BroadcastNavGraph.root))
}
