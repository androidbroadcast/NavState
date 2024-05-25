package dev.androidbroadcast.navstate

import androidx.compose.runtime.Composable
import com.arkivanov.essenty.backhandler.BackDispatcher

@Composable
internal actual fun platformBackDispatcher(): BackDispatcher = BackDispatcher()