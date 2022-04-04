package net.chmielowski.randomchoice.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import net.chmielowski.randomchoice.core.Label

@Suppress("FunctionName")
@Composable
internal fun Flow<Label>.Observe(action: suspend (value: Label) -> Unit) {
    LaunchedEffect(Unit) {
        collectLatest(action)
    }
}
