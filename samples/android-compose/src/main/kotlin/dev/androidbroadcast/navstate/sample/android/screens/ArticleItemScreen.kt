package dev.androidbroadcast.navstate.sample.android.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import dev.androidbroadcast.navstate.annotations.NavDest
import dev.androidbroadcast.navstate.sample.android.BroadcastNavGraph
import dev.androidbroadcast.navstate.sample.android.data.ArticleId

@Composable
@NavDest(BroadcastNavGraph.Article::class)
fun ArticleItemScreen(articleId: ArticleId) {
    Text("Article Item $articleId")
}
