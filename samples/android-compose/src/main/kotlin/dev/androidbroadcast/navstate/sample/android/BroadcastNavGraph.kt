package dev.androidbroadcast.navstate.sample.android

import dev.androidbroadcast.navstate.NavDest
import dev.androidbroadcast.navstate.sample.android.data.ArticleId
import kotlinx.serialization.Serializable

sealed interface BroadcastNavGraph : NavDest {

    @Suppress("CanSealedSubClassBeObject")
    @Serializable
    class NavMenu : BroadcastNavGraph

    @Suppress("CanSealedSubClassBeObject")
    @Serializable
    class Articles : BroadcastNavGraph

    @Serializable
    data class Article(val articleId: ArticleId) : BroadcastNavGraph

    @Suppress("CanSealedSubClassBeObject")
    @Serializable
    class BroadcastResources : BroadcastNavGraph

    @Suppress("CanSealedSubClassBeObject")
    @Serializable
    class AboutAuthor : BroadcastNavGraph

    @Serializable
    data class WebPageDest(val url: String) : NavDest

    companion object {

        val root = NavMenu()
    }
}
