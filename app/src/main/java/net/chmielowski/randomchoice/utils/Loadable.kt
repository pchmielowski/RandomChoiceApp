package net.chmielowski.randomchoice.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import net.chmielowski.randomchoice.utils.Loadable.Loaded
import net.chmielowski.randomchoice.utils.Loadable.Loading

internal sealed interface Loadable<T> {

    object Loading : Loadable<Nothing>

    data class Loaded<T>(val content: T) : Loadable<T>
}

@SuppressLint("FlowOperatorInvokedInComposition")
@Composable
internal fun <T> Flow<T>.collectAsLoadableState() = this
    .map { Loaded(it) }
    .collectAsState(Loading)
