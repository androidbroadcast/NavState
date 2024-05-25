package dev.androidbroadcast.navstate

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.backhandler.BackDispatcher
import androidx.activity.compose.BackHandler as AndroidXBackHandler

@Composable
internal actual fun platformBackDispatcher(): BackDispatcher {
    return BackDispatcher().also { dispatcher ->
        AndroidXBackHandler { dispatcher.back() }
    }
}